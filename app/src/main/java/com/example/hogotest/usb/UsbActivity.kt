package com.example.hogotest.usb

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.hugo.mylibrary.components.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashMap

@AndroidEntryPoint
class UsbActivity :BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val usbDevices = resolveDevices()
        setContent {
            UsbList(usbDevices = usbDevices)
        }
    }

    fun resolveDevices(): HashMap<String, UsbDevice> {
        val usbManager = getSystemService(USB_SERVICE) as UsbManager
        return usbManager.deviceList
    }
}
@Composable
fun UsbList(usbDevices: Map<String,UsbDevice>){
    LazyColumn {
        items(usbDevices.size){index->
            val entity = usbDevices.entries.toList()[index]
            UsbItem(
                key = entity.key,
                usbDevice = entity.value)
        }
    }

}

@Composable
fun UsbItem(key:String,usbDevice: UsbDevice){

    LazyColumn(Modifier.padding(start = 5.dp).fillMaxWidth().height(500.dp)) {
      item {
          Text(text = key)
      }
       items(usbDevice.interfaceCount){
           val entity = usbDevice.getInterface(it)
           UsbInterface(usbInterface = entity)

       }


    }

}


@Composable
fun UsbInterface(usbInterface: UsbInterface){


        LazyColumn(Modifier.padding(start = 5.dp).fillMaxWidth().height(300.dp)) {
            item {
                Text(text = usbInterface.name?:usbInterface.toString())
            }
            items(usbInterface.endpointCount){
                val entity = usbInterface.getEndpoint(it)
                com.example.hogotest.usb.UsbEndpoint(usbEndPoint = entity)
            }
        }


}

@Composable
fun UsbEndpoint(usbEndPoint: UsbEndpoint){
    Column(Modifier.padding(start = 5.dp).fillMaxWidth().height(IntrinsicSize.Min)) {
        Text(text = usbEndPoint.toString())

    }
}

class UsbViewModel():ViewModel(){
    val usbDeviceList = mutableListOf<UsbDevice>()
    fun addDevice(usbDevice: UsbDevice){
        usbDeviceList.add(usbDevice)
    }



}
