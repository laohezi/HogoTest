package com.example.hogotest.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.hogotest.utils.Stopwatch
import kotlinx.coroutines.delay

class BlueToothActivity : AppCompatActivity() {
    val viewModel by viewModels<BlueToothViewModel>()
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    var permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(
        )
    ) {
        toggleBlutooth(true)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.stateOpen = bluetoothAdapter.isEnabled

        setContent {

        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        )


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            viewModel.stateOpen = resultCode == Activity.RESULT_OK
        }
    }


    fun startScan() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        registerReceiver(bludtoothReceiver, intentFilter)
    }

    val bludtoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }

    }

    @SuppressLint("MissingPermission")
    fun toggleBlutooth(enable: Boolean) {


        if (enable) {
            bluetoothAdapter.enable()
        } else {
            bluetoothAdapter.disable()
        }
        viewModel.stateOpen = bluetoothAdapter.isEnabled
    }
}

class BlueToothViewModel : ViewModel() {
    var hasPermission by mutableStateOf(false)
    var stateOpen by mutableStateOf(false)
    val devices = mutableStateListOf<BluetoothDevice>()

}

@Composable
fun StatedPage(viewModel: BlueToothViewModel,requestPermissionAction:()->Unit){
    if (viewModel.hasPermission){

    }else{
        RequestPermissionPage(requestPermissionAction)
    }
}

@Composable
fun RequestPermissionPage(requestPermissionAction: () -> Unit){

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            requestPermissionAction()
        }) {
            Text(text = "请求权限")
        }
    }
}

@Preview

@Composable
fun RequestPermissionPagePreview(){
    RequestPermissionPage({})
}



@Preview
@Composable
fun BluetoothPagePreview() {

}

@Composable
fun BluetoothPage(viewModel: BlueToothViewModel) {

}


@SuppressLint("MissingPermission")
@Composable

fun BluetoothDevice(device: BluetoothDevice){
    Column {
      Text(text = device.name!!)

    }

}

@Composable
@Preview
fun BluetoothDevicePreview(){

}




