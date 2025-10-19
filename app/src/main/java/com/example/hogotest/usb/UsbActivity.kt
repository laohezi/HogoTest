package com.example.hogotest.usb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import com.hugo.mylibrary.components.BaseActivity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap


class UsbActivity : BaseActivity() {
    val actionUsbPermission = "com.example.hogotest.USB_PERMISSION"

    val usbManager by lazy {
        getSystemService(USB_SERVICE) as UsbManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usbDevices = resolveDevices()
        setContent {
            UsbList(usbDevices = usbDevices, onClickDevice = { usbDevice ->
                tryToOpenDevice(usbDevice)
            })
        }
    }

    suspend fun openDevice(usbDevice: UsbDevice) {
        val connection = usbManager.openDevice(usbDevice)
        if (connection != null) {

        }
    }

    fun resolveDevices(): HashMap<String, UsbDevice> {
        return usbManager.deviceList
    }

    fun tryToOpenDevice(usbDevice: UsbDevice) = CoroutineScope(Dispatchers.IO).launch {
        if (!usbManager.hasPermission(usbDevice)) {
            requestPermission(usbDevice)
        } else {
            openDevice(usbDevice)
        }

    }


    fun requestPermission(usbDevice: UsbDevice) {
        if (!usbManager.hasPermission(usbDevice)) {
            val intentFiler = IntentFilter(actionUsbPermission)
            registerReceiver(devicePermissionReceiver, intentFiler)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, Intent(actionUsbPermission),
                PendingIntent.FLAG_IMMUTABLE
            )
            usbManager.requestPermission(usbDevice, pendingIntent)
        }
    }


    val devicePermissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val usbDevice = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
            if (usbDevice != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    openDevice(usbDevice)
                }
            }
        }


    }

    @Composable
    fun UsbList(usbDevices: Map<String, UsbDevice>, onClickDevice: (usbDevice: UsbDevice) -> Unit) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            usbDevices.forEach { entity ->
                UsbItem(
                    key = entity.key,
                    usbDevice = entity.value,
                    onClick = { onClickDevice(entity.value) }
                )
            }
        }

    }

    @Composable
    fun UsbItem(key: String, usbDevice: UsbDevice, onClick: () -> Unit = {}) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        )
        {

            Text(text = "The Device is $key",Modifier.background(color = Color.Gray))
            for (i in 0 until usbDevice.interfaceCount) {
                val entity = usbDevice.getInterface(i)
                UsbInterface(usbInterface = entity)
            }
        }
    }


    @Composable
    fun UsbInterface(usbInterface: UsbInterface) {


        Column(
            Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()
        ) {

            Text(text = usbInterface.name ?: usbInterface.toString(),Modifier.background(color = Color.LightGray))
            for (i in 0 until usbInterface.endpointCount) {
                val entity = usbInterface.getEndpoint(i)
                UsbEndpoint(usbEndPoint = entity)
                Divider(color = Color.Gray)
            }
        }


    }

    @Composable
    fun UsbEndpoint(usbEndPoint: UsbEndpoint) {
        Column(
            Modifier
                .padding(start = 5.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(text = usbEndPoint.toString())

        }
    }
}

