package com.example.app1.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import com.example.app1.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class SeriesPcuDetailModel() {


    fun getPageDate(id: String?): JSONObject {
        return FBYJsonParser.getJSONFormAssets("detail_page.json")!!
    }

    fun getBanner(json: JSONObject): Any? {
        return FBYJsonParser.getJSONFormAssets("banner.json")
        // return  getBannerFromLocal()
    }

}


data class SeriesPcuDetailSampleItem(
    val sampleUrl: String
)

/***
 * The later items  was regard as the child of previous items. if the same key item was update
 * ,the relation will not update
 *
 * */
class KeyTreeMap<K, V>(val map: MutableMap<K, V> = HashMap<K, V>()) {
    val keys = ArrayList<K>()
    fun put(key: K, value: V) {
        if (!keys.contains(key)) {
            keys.add(key)
        }
        map[key] = value
    }

    fun clearChild(key: String) {
        /* val position = keys.indexOf(key)
         if (position >= 0 && position < keys.size - 1) {
             for (i in position + 1 until keys.size) {
                 map.remove(keys[i])
             }
             keys.subList(position + 1, keys.size - 1).clear()
         }*/
    }

}









