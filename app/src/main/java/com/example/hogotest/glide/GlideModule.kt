package com.example.hogotest.glide

import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.module.GlideModule
import com.bumptech.glide.signature.ObjectKey
import com.example.hogotest.opengl.MyGlSurfaceView
import java.io.InputStream
import java.nio.ByteBuffer

@com.bumptech.glide.annotation.GlideModule
class MyGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

     registry.prepend(String::class.java,ByteBuffer::class.java,Base64ModelLoaderFactory())

    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor())


        super.applyOptions(context, builder)

    }
}

fun test(){
    //Glide.with(Activity()).load("").error(1).placeholder(2)

}

// Path: app/src/main/java/com/example/hogotest/glide/OkHttpUrlLoader.kt

class Base64ModelLoader:ModelLoader<String,ByteBuffer>{
    override fun buildLoadData(
        model: String,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<ByteBuffer>? {
        return ModelLoader.LoadData(ObjectKey(model),Base64Fetcher(model))
    }

    override fun handles(model: String): Boolean {
       return  model.startsWith("data:")
    }



    class  Base64Fetcher:DataFetcher<ByteBuffer>{
        val model:String
        constructor( model: String){
            this.model = model
        }
        override fun loadData(
            priority: Priority,
            callback: DataFetcher.DataCallback<in ByteBuffer>
        ) {
            val startOfBase64 = model.indexOf(',')
            val data = Base64.decode(model.substring(startOfBase64+1),Base64.DEFAULT)
            val byteBuffer = ByteBuffer.wrap(data)
            callback.onDataReady(byteBuffer)

        }

        override fun cleanup() {

        }

        override fun cancel() {

        }

        override fun getDataClass(): Class<ByteBuffer> {
            return  ByteBuffer::class.java

        }

        override fun getDataSource(): DataSource {
               return  DataSource.LOCAL
        }

    }

}

class Base64ModelLoaderFactory :ModelLoaderFactory<String,ByteBuffer>{
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, ByteBuffer> {
       return Base64ModelLoader()
    }

    override fun teardown() {

    }

}