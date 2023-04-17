package com.example.hogotest.glide

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.module.GlideModule
import com.example.hogotest.opengl.MyGlSurfaceView
import java.io.InputStream
import java.nio.ByteBuffer

class MyGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
     //   registry.append()
          val data = MutableLiveData(1)


    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {

        super.applyOptions(context, builder)

    }
}

fun test(){
    //Glide.with(Activity()).load("").error(1).placeholder(2)

}

// Path: app/src/main/java/com/example/hogotest/glide/OkHttpUrlLoader.kt

class Base64ModelLoader :ModelLoader<String, ByteBuffer> {
    override fun buildLoadData(
        model: String,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<ByteBuffer>? {
        return ModelLoader.LoadData(GlideUrl(model), Base64DataFetcher(model))
    }

    class Base64DataFetcher(val model: String) : DataFetcher<ByteBuffer> {
        override fun loadData(
            priority: Priority,
            callback: DataFetcher.DataCallback<in ByteBuffer>
        ) {
            val byte = model.toByte()
        }

        override fun cleanup() {
            TODO("Not yet implemented")
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

        override fun getDataClass(): Class<ByteBuffer> {
            return ByteBuffer::class.java
        }

        override fun getDataSource(): DataSource {
           return DataSource.RESOURCE_DISK_CACHE
        }

    }

    override fun handles(model: String): Boolean {
        return model.startsWith("data:image")
    }
}