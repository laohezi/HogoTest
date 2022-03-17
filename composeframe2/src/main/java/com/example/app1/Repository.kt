package com.example.app1

import android.text.TextUtils
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


val baseUrl = "https://fp-fpa15038-api.yastage.planetart.com"

val versionName = "3.34.0.38641"

val userAgent = "FREEPRINT Android/$versionName"


interface Api{
    @FormUrlEncoded
    @POST("/api/site.get_product_data")
    fun getPCUProductData(@FieldMap filedMap: Map<String, String>): JSONObject
}



