package com.example.hogotest.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
            StopWatchTest()
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
    var stateOpen by mutableStateOf(false)


}

val key = "blue tooth"


@Preview
@Composable
fun Bluetooth() {
    Bluetooth(switchBlueTooth = { it ->


    }, false)
}


@Composable
fun Bluetooth(switchBlueTooth: (Boolean) -> Unit, viewModel: BlueToothViewModel) {

    Column() {
        Switch(checked = viewModel.stateOpen, onCheckedChange = {
            switchBlueTooth(it)
        })
    }
}


@Composable
fun Bluetooth(switchBlueTooth: (Boolean) -> Unit, checked: Boolean) {

    Column() {
        Switch(checked = checked, onCheckedChange = {
            switchBlueTooth(it)
        })
    }
}

@Preview
@Composable
fun StopWatchTest() {
    val stopwatch = remember {
        Stopwatch(10 * 1000)
    }
    var time by remember {
        mutableStateOf(0L)
    }
    LaunchedEffect(key1 = stopwatch) {
        while (true) {
            time = stopwatch.getElapsedTime()
            delay(100)
        }
    }

    Column {
        Row() {
            Button(
                onClick = {
                    stopwatch.start()
                }) {

            }

            Button(onClick = {
                stopwatch.stop()
            }) {}

            Button(onClick = {
                stopwatch.pause()
            }) {}

            Button(onClick = {
                stopwatch.resume()
            }) {}
        }

        Text(text = time.toString())

    }


}


class SingleTon {
    companion object {
        val instance by lazy {
            SingleTon()
        }

    }

    private constructor() {

    }


}

