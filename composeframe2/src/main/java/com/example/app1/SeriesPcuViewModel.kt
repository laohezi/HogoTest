package com.example.app1

import SeriesPcuCategoryItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SeriesPcuViewModel : ViewModel() {
    val detailGroup = MutableLiveData<SeriesPcuCategoryItem>()
}