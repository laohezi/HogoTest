package com.example.hogotest.ui

 import android.content.Context
 import android.content.Intent
 import android.content.ServiceConnection
 import android.content.ComponentName
 import android.os.IBinder
 import android.os.ParcelFileDescriptor
 import android.util.Log
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
 import androidx.compose.material3.Button
 import androidx.compose.material3.Text
 import androidx.compose.material3.TextField
 import androidx.compose.runtime.Composable
 import androidx.compose.runtime.DisposableEffect
 import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.remember
 import androidx.compose.runtime.rememberCoroutineScope
 import androidx.compose.runtime.setValue
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.platform.LocalContext
 import androidx.compose.ui.unit.dp
 import com.example.hogotest.di.ioScope
 import com.hugo.mylibrary.IFileInterface
 import androidx.compose.runtime.collectAsState
 import kotlinx.coroutines.CoroutineScope
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.flow.MutableStateFlow
 import kotlinx.coroutines.flow.StateFlow
 import kotlinx.coroutines.flow.collectLatest
 import kotlinx.coroutines.launch
 import kotlinx.coroutines.withContext
 import kotlinx.coroutines.Job
 import kotlinx.coroutines.isActive
 import kotlinx.coroutines.delay
 import kotlinx.coroutines.sync.Mutex
 import kotlinx.coroutines.sync.withLock
 import java.io.FileInputStream
 import java.io.FileOutputStream
 import java.io.IOException

 @Composable
 fun FileDescriptorTestScreen() {
     val context = LocalContext.current
     val client = remember { FileDescriptorClient(context) }
     val connected by client.connected.collectAsState(initial = false)
     var inputText by remember { mutableStateOf("") }
     val scope = rememberCoroutineScope()

     DisposableEffect(Unit) {
         client.bind()
         onDispose { client.unbind() }
     }

     Column(modifier = Modifier.padding(16.dp)) {
         TextField(
             value = inputText,
             onValueChange = { inputText = it },
             label = { Text("Enter a number (0-254)") }
         )
         Spacer(modifier = Modifier.height(8.dp))
         Button(
             onClick = {
                 scope.launch {
                     val number = inputText.trim().toIntOrNull() ?: 0
                     val response = client.request(number)
                     Log.d("FileDescriptorClient", "Received from server: $response")
                 }
             },
             enabled = connected
         ) {
             Text("Send to Server")
         }

         Button(
             onClick = {
                    scope.launch {
                        val success = client.reconnectSharedFd()
                        Log.d("FileDescriptorClient", "reconnectSharedFd success: $success")
                    }
             }
         ){Text("reconnectSharedFd")
         }
     }
 }

 class FileDescriptorClient(
     context: Context,
     coroutineScope: CoroutineScope = ioScope
 ) {
     private val appContext = context.applicationContext
     private val ifaceFlow = MutableStateFlow<IFileInterface?>(null)
     private val _connected = MutableStateFlow(false)
     val connected: StateFlow<Boolean> get() = _connected

     private var localFd: ParcelFileDescriptor? = null
     private var readerStream: FileInputStream? = null
     private var writerStream: FileOutputStream? = null
     private var readerJob: Job? = null



     init {
         coroutineScope.launch {
             ifaceFlow.collectLatest { iface ->
                 _connected.value = (iface != null)
                 reconnectSharedFd()

             }
         }
     }

     private fun closeCurrent() {
         Log.d(TAG, "closeCurrent: cleaning up previous fd/streams/job")
         try {
             readerJob?.cancel()
             readerJob = null
             readerStream?.close()
             readerStream = null
             writerStream?.close()
             writerStream = null
             localFd?.close()
             localFd = null
         } catch (e: Exception) {
             Log.e(TAG, "closeCurrent: error closing resources", e)
         }
     }

     private val connection = object : ServiceConnection {
         override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
             ifaceFlow.value = IFileInterface.Stub.asInterface(service)

             _connected.value = true

         }

         override fun onServiceDisconnected(name: ComponentName?) {
             ifaceFlow.value = null
             _connected.value = false
             closeCurrent()
         }
     }

     fun bind() {

         val intent = Intent("com.example.aldlserver.FileDescriptorService").apply {
             `package` = "com.example.aldlserver"
         }
         val bound = appContext.bindService(intent, connection, Context.BIND_AUTO_CREATE)
         Log.d(TAG, "bind: result=$bound")
     }

     fun unbind() {

         try {
             appContext.unbindService(connection)
         } catch (_: Exception) {}
         closeCurrent()
         ifaceFlow.value = null
         _connected.value = false
     }

     suspend fun request(number: Int) {
         val iface = ifaceFlow.value ?: return

         return withContext(Dispatchers.IO) {
             try {
                val out = writerStream ?: throw IOException("no writerStream")
                val inStream = readerStream ?: throw IOException("no readerStream")
                out.write(number)
                out.flush()
                Log.d(TAG, "request: sent $number, waiting for response...")
             } catch (e: Exception) {
                 Log.e(TAG, "request error", e)
                 null
             }
         }
     }

     // 重新共享 fd：先发一个占位（关闭端）给服务端，然后创建新的 pair 并替换当前 localFd/streams
     suspend fun reconnectSharedFd(): Boolean {
         val iface = ifaceFlow.value ?: return false
         return withContext(Dispatchers.IO) {
             try {
                Log.d(TAG, "reconnectSharedFd: step1 send null fd to server")
                 iface.shareFd(null)

                 Log.d(TAG, "reconnectSharedFd: step2 closeCurrent")
                 closeCurrent()

                 Log.d(TAG, "reconnectSharedFd: step3 create new fd pair")
                 val newPair = ParcelFileDescriptor.createReliableSocketPair()
                 val newLocal = newPair[0]
                 val newServer = newPair[1]
                 localFd = newLocal
                 readerStream = FileInputStream(newLocal.fileDescriptor)
                 writerStream = FileOutputStream(newLocal.fileDescriptor)
                 Log.d(TAG, "reconnectSharedFd: step4 share new fd to server")
                 iface.shareFd(newServer)
                 Log.d(TAG, "reconnectSharedFd: step5 close newServer fd locally")
                 newServer.close()
                 Log.d(TAG, "reconnectSharedFd: step6 startPersistentReader")
                 startPersistentReader()
                 true
             } catch (e: Exception) {
                 Log.e(TAG, "reconnectSharedFd failed", e)
                 false
             }
         }
     }

     private fun startPersistentReader() {
         try { readerJob?.cancel() } catch (_: Exception) {}
         val inStream = readerStream ?: return
         readerJob = ioScope.launch(Dispatchers.IO) {
             val buf = ByteArray(1024)
             try {
                 while (isActive) {
                     val r = inStream.read(buf)
                     if (r == -1) {
                         Log.w(TAG, "persistent reader: stream closed, exiting")
                         break

                     }
                     Log.d(TAG, "read from server: first=${buf[0].toInt() and 0xFF}, len=$r")
                     delay(10)
                 }
             } catch (e: Exception) {
                 Log.e(TAG, "persistent reader error", e)
             }
         }
     }

     companion object {
         private const val TAG = "FileDescriptorClient"
     }
 }
