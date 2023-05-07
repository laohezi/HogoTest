package com.example.hogotest

import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import java.lang.reflect.Field

interface ApiStore {
    @Multipart
    @POST("uploadImage")
    @Headers(

    )
    fun uploadImage( @Part() imageFile:File,@Part describe:JSONObject)

}



fun upload(filePath:String){

    val  file = File(filePath)
    val describe = JSONObject().apply {
        put("name",file.name)
       // put("size",file.)
    }
    apiStor.uploadImage(file,describe)


}



val apiStor   = Retrofit.Builder()
    .baseUrl("")
    .build()
    .create<ApiStore>(ApiStore::class.java);




