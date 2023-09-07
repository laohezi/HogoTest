package com.example.hogotest.webview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.blankj.utilcode.util.ToastUtils

class WebViewActivity :AppCompatActivity(){
    val webView:WebView by lazy {
        WebView(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

        }
    }

    val root:LinearLayout by lazy {
        LinearLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)

            orientation = LinearLayout.VERTICAL

        }
    }
    val callJSParams = "I am from native"
    val callJsButton:Button by lazy {
        AppCompatButton(this).apply {
             text = "Call JS function"
            setOnClickListener {
            webView.post {
               // webView.loadUrl("javascript:callJS(\"$callJSParams \")")
                webView.evaluateJavascript("javascript:callJS(\"$callJSParams \")"
                ) { ToastUtils.showShort(it) }
            }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root.addView(callJsButton)
        root.addView(webView)

        setContentView(root)
        initWebview()
    }

    private fun initWebview() {
        webView.loadUrl("http://192.168.3.93:3000")
        val settings = webView.settings
        with(settings){
            javaScriptEnabled = true

        }
        webView.webViewClient = object :WebViewClient(){

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

        }

        webView.webChromeClient = object :WebChromeClient(){

            override fun onJsAlert(
                view: WebView?,
                url: String?,
                message: String?,
                result: JsResult?
            ): Boolean {

               return  super.onJsAlert(view, url, message, result)
            }

        }

        webView.addJavascriptInterface(JSInterface(),"JSInterface")
    }

}

class JSInterface{
    @JavascriptInterface
    fun sayHello(params:String){
       ToastUtils.showShort(params)
    }
}