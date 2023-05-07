package com.example.hogotest

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.aldlserver.IPlayer
import me.jessyan.autosize.external.ExternalAdaptManager
import kotlin.concurrent.thread

class AIDLClientActivity :AppCompatActivity(){

    lateinit var player:IPlayer

    val  serviceConnection = object :ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            player = IPlayer.Stub.asInterface(p1)
            Log.d("huhuhu",player.play())
            Log.d("huhuhu",player.pause())


        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            Glide.with(this@AIDLClientActivity)
                .load("")
                .into(ImageView(this@AIDLClientActivity))
        }




    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent()
        intent.action = "com.example.aldlserver.PlayerService"
        intent.setPackage("com.example.aldlserver")
        intent.setComponent(ComponentName("com.example.aldlserver","com.example.aldlserver.PlayerService"))
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)

    }



}