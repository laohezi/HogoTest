package com.example.aldlserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.ParcelFileDescriptor
import android.util.Log
import com.hugo.mylibrary.IFileInterface
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.*

class FileDescriptorService : Service() {
    private val binder: IFileInterface.Stub = object : IFileInterface.Stub() {
        var fd: ParcelFileDescriptor? = null
        var inputStream: FileInputStream? = null
        var outputStream: FileOutputStream? = null
        private var readerJob: Job? = null
        private val binderJob = Job()
        private val scope = CoroutineScope(Dispatchers.IO + binderJob)

        // 统一关闭逻辑
        private fun closeCurrent() {
            Log.d(TAG, "closeCurrent: cleaning up previous fd/streams/job")
            try {
              //  readerJob?.cancel()
              //  readerJob = null
                inputStream?.close()
                inputStream = null
                outputStream?.flush()
                outputStream?.close()
                outputStream = null
             //   fd?.close()
             //   fd = null
            } catch (e: Exception) {
                Log.e(TAG, "closeCurrent: error closing resources", e)
            }
        }

        override fun shareFd(pfd: ParcelFileDescriptor?) {
            Log.d(TAG, "shareFd called, fd=$pfd")
            closeCurrent()
            if (pfd == null) {
                Log.d(TAG, "shareFd: received null fd, connection will be closed")
                return
            }
            fd = pfd
            try {
                inputStream = ParcelFileDescriptor.AutoCloseInputStream(pfd)
                outputStream = ParcelFileDescriptor.AutoCloseOutputStream(pfd)
            } catch (e: Exception) {
                Log.e(TAG, "shareFd: failed to create streams", e)
                inputStream = null
                outputStream = null
            }
            readerJob = scope.launch {
                Log.d(TAG, "readerJob: started for fd=$pfd")
                val fis = inputStream
                if (fis == null) {
                    Log.w(TAG, "readerJob: no inputStream, exiting")
                    return@launch
                }
                val buf = ByteArray(1024)
                var steamReady = true
                try {
                    while (isActive && steamReady) {
                        val r = fis.read(buf)
                        // 直接将 byte 数组转为 int，转不了就抛异常
                        val value = buf[0].toInt()

                        Log.d(TAG, "readerJob: Received int value: $value")
                        val response = value + 1
                        writeResponse(response)
                        if (response > 100) {
                            Log.d(TAG, "readerJob: Closing streams as data > 255")
                            throw Exception("Data exceeded 255, closing connection")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "readerJob: Error reading from inputStream", e)
                    closeCurrent()
                    steamReady = false
                }
                Log.d(TAG, "readerJob: finished for fd=$pfd")
            }
        }

        private fun writeResponse(response: Int) {
            try {
                val out = outputStream ?: return
                Log.d(TAG, "writeResponse: writing response $response")
                out.write(response)
                out.flush()
            } catch (e: Exception) {
                Log.e(TAG, "writeResponse: failed", e)
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    companion object {
        private const val TAG = "FileDescriptorService"
    }
}
