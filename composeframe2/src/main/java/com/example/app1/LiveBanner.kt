package com.example.app1

import android.annotation.SuppressLint
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app1.utils.hexToColor
import com.example.app1.utils.px2sp
import org.json.JSONArray
import org.json.JSONObject
import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.example.app1.detail.SeriesPcuDetailViewModel
import com.example.app1.utils.px2dp
import java.lang.Exception


@Preview
@Composable
fun PreviewLiveBanner() {
    LiveBannerView(json = FBYJsonParser.getJSONFormAssets("banner.json")!!)
}

@Composable
fun LiveBannerView(viewModel: SeriesPcuDetailViewModel){
    val json = viewModel.pageData.banner.observeAsState()
     json.value?.let { LiveBannerView(json = it as JSONObject) }
}



@SuppressLint("UnrememberedMutableState")
@Composable
fun LiveBannerView(json: JSONObject?) {
    if (json == null){
        return
    }
    val bgColor = json.optJSONObject("background")?.optString("color")
    val content = getByDeviceType(json = json)
    var paddingTop  by remember { mutableStateOf(0f) }
    var paddingBottom  by remember { mutableStateOf(0f) }

    Column(
        Modifier
            .background(
                color = hexToColor(bgColor),
            )
            .padding(top = paddingTop.dp, bottom = paddingBottom.dp)
            .clickable {
                paddingTop = 30f
            }
    ) {
        for (i in 0 until content.length()) {
            val obj = content.getJSONObject(i)
            if (i ==0){
                paddingTop = designSizeToDp(obj.optDouble("top_padding"))
                Log.d("test", "LiveBannerView: padding top is $paddingTop")
            }
            if (i== content.length() -1){
                paddingBottom = designSizeToDp(obj.optDouble("bottom_padding"))
            }
            when (obj.optString("type")) {
                "text" -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = obj.optString("content"),
                        color = hexToColor(obj.optString("color")),
                        textAlign = TextAlign.Center,
                        fontSize = designSizeToSp(obj.optDouble("size")).sp,
                        fontFamily = FontFamily(getTypeFace(obj.optString("font")))
                    )
                }
            }

        }

    }
}

private fun designSizeToSp(designSize: Double): Float {
    return px2sp(designSizeToPX(designSize = designSize))
}

private fun designSizeToDp(designSize: Double): Float {
    return px2dp(designSizeToPX(designSize = designSize))
}

private fun designSizeToPX(designSize: Double): Float {
    return getSizeAfterScale(designSize).toFloat()
}

private fun getByDeviceType(json: JSONObject): JSONArray {
    return when (checkDeviceType(appContext)) {
        DeviceType.PAD_3x4 -> json.optJSONArray("ipad")
        DeviceType.PHONE_3_5 -> json.optJSONArray("normal")
        DeviceType.PHONE_1x2 -> json.optJSONArray("s8")
        DeviceType.DEFAULT -> json.optJSONArray("normal")
    }
}


private fun getTypeFace(fontName: String?, fallback: Typeface = Typeface.DEFAULT): Typeface {

    if (!TextUtils.isEmpty(fontName)) {
        var font: Typeface? = null
        try {
            font = Typeface.createFromAsset(
                appContext.getResources().getAssets(),
                String.format("fonts/%s", fontName)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            font = fallback
        }
        return font!!

    } else {
        return fallback
    }
}


