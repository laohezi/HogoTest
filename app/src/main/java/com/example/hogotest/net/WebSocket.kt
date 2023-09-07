package com.example.hogotest.net

import android.os.Bundle
import com.hugo.mylibrary.components.BaseActivity
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketActivity:BaseActivity(){

   val httpClient = OkHttpClient.Builder()
       .pingInterval(3,TimeUnit.SECONDS)
       .build()
    val webSocketUrl = "ws://"
    val request = Request.Builder().url(webSocketUrl).build()
    var webSocket:WebSocket? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpClient.newWebSocket(request,object :WebSocketListener(){

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@WebSocketActivity.webSocket = webSocket


            }

        })
    }

}