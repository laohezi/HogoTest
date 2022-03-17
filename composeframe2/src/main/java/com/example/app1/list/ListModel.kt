package com.example.app1.list

import SeriesPcuCategoryItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.app1.FBYJsonParser
import kotlinx.coroutines.delay
import org.json.JSONObject

class ListViewModel :ViewModel() {

    var banner by mutableStateOf<Any?>(null)
    var items =  mutableStateListOf<SeriesPcuCategoryItem>()

    suspend fun getListData(): JSONObject {
        delay(500)
        return FBYJsonParser.getJSONFormAssets("list_page.json")!!
            .also {
               val list =  it.optJSONArray("list")
                for (i in 0 until  list.length()){
                    items.add(SeriesPcuModel.jsonToCategory(list.optJSONObject(i)))
                }
                banner = it.optString("banner").replace("%@", "_aspect_ratio_2")
            }
    }

}