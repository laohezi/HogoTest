package com.example.app1.detail

import SeriesPcuCategoryItem
import SeriesPcuModel.jsonToCategory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app1.PageState
import com.example.app1.SeriesPcuViewModel
import kotlinx.coroutines.*
import org.json.JSONObject

class SeriesPcuDetailViewModel : ViewModel() {

    var model = SeriesPcuDetailModel()
    lateinit var impl: ViewModelImpl
    val pageState = MutableLiveData<PageState>(PageState.Loading)
    var pageData = PageData()
    var currentChild = MutableLiveData<SeriesPcuCategoryItem>()
    val selectMap = MutableLiveData<MutableMap<String, SeriesPcuCategoryItem>>(LinkedHashMap())

    var currentId = ""
    val templateRequest = MutableLiveData<RequestState>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            pageState.value = PageState.Error(throwable)
        }


    fun initData(reset: Boolean = true) {

       /* if (selectedId != null && currentId == selectedId && pageState.value !is PageState.Error) {
            return
        }
        if (reset) {
            reset()
        }*/
        //currentId = selectedId!!
        viewModelScope.launch(coroutineExceptionHandler) {
            pageState.value = PageState.Loading
            try {
                _initData(this + Dispatchers.IO)
                pageState.value = PageState.Complete
            } catch (e: Exception) {
                pageState.value = PageState.Error(e)
                e.printStackTrace()
            }
        }
    }

    private fun setLiveBanner(json: JSONObject) {
        try {
            val banner = model.getBanner(json)
            pageData.banner.postValue(banner!!)
        } catch (e: Exception) {

        }
    }

    private fun parsePageData(json: JSONObject) {
        impl.parsePageDate(json)
    }


    fun validate(): Boolean {
        return impl.validate().apply { pageData.dataValid.value = this }
    }

    fun onSelectItem(position: Int, item: SeriesPcuCategoryItem) {
        currentChild.value = item
        impl.onSelectItem(position, item)
        validate()
    }

    fun reset() {
        currentChild = MutableLiveData()
        selectMap.value?.clear()
        pageData.reset()
    }

    fun isLeafNode(item: SeriesPcuCategoryItem?): Boolean {
        return impl.isLeafNode(item)
    }


    private suspend fun _initData(scope: CoroutineScope): JSONObject {
        return withContext(scope.coroutineContext) {
            var json: JSONObject = model.getPageDate("")
            if (!json.optBoolean("result")) {
                throw Exception(json.optString("message"))
            }
            setLiveBanner(json)


            impl = TreeImpl(this@SeriesPcuDetailViewModel)

            withContext(Dispatchers.Main) {
                parsePageData(json)
            }
            return@withContext json
        }
    }

    /*fun groupIsLeafNode(): Boolean {
        return isLeafNode(flowViewModel.detailGroup.value)
    }*/
}


class TreeImpl(viewModel: SeriesPcuDetailViewModel) : ViewModelImpl(viewModel) {
    val keyTreeMap = KeyTreeMap(viewModel.selectMap.value!!)
    override fun onSelectItem(position: Int, item: SeriesPcuCategoryItem) {
        keyTreeMap.put(item.childType, item)
        keyTreeMap.clearChild(item.childType)
        item.clearStateRecursive()
    }

    override fun isLeafNode(item: SeriesPcuCategoryItem?): Boolean {
        return if (item != null) {
            item.childs.size < 1 || item.childType.isNullOrEmpty()
        } else {
            false
        }
    }

    override fun parsePageDate(json: JSONObject) {
        val pageData = viewModel.pageData
        val top = jsonToCategory(json)
        pageData.options.postValue(arrayListOf(top))
        pageData.pcuDes.postValue(top.title)
        pageData.price.postValue(top.price)
        pageData.productInfo.postValue(top.productInfo)
        val thumbnails = ArrayList<SeriesPcuDetailSampleItem>()
        val thumbnailsJson = json.optJSONArray("thumbnails")
        thumbnailsJson?.let {
            for (i in 0 until it.length()) {
                val jsonItem = it.optJSONObject(i)
                thumbnails.add(
                    SeriesPcuDetailSampleItem(
                        sampleUrl = jsonItem.optString("url")
                    )
                )
            }
        }
        pageData.samples.postValue(thumbnails)
    }
}


/**
 *   include initial page data which was immutable
 * */
data class PageData(
    var banner: MutableLiveData<Any> = MutableLiveData(),
    val pcuDes: MutableLiveData<String> = MutableLiveData(),
    val price: MutableLiveData<String> = MutableLiveData(),
    val samples: MutableLiveData<MutableList<SeriesPcuDetailSampleItem>> = MutableLiveData(ArrayList()),
    val options: MutableLiveData<MutableList<SeriesPcuCategoryItem>> = MutableLiveData(ArrayList()),
    val productInfo: MutableLiveData<String> = MutableLiveData(),
    val dataValid: MutableLiveData<Boolean> = MutableLiveData(false)
) {
    fun reset() {
        dataValid.value = false
    }
}

sealed class RequestState {
    object Start : RequestState()
    class Error(e: Throwable) : RequestState()
    class Complete(val data: Any? = null) : RequestState()
}

abstract class ViewModelImpl(val viewModel: SeriesPcuDetailViewModel) {
    abstract fun onSelectItem(position: Int, item: SeriesPcuCategoryItem)


    open fun validate(): Boolean {
        return isLeafNode(viewModel.currentChild?.value)
    }

    abstract fun isLeafNode(item: SeriesPcuCategoryItem?): Boolean


    open fun parsePageDate(json: JSONObject) {

    }


}




