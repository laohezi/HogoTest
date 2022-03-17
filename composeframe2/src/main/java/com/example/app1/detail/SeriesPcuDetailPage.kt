package com.example.app1.detail

import SeriesPcuCategoryItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app1.LiveBannerView
import com.example.app1.NavigationViewModel
import com.example.app1.R
import com.example.app1.Screen
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import org.json.JSONObject


@ExperimentalPagerApi
@Composable
fun DetailPage(viewModel: SeriesPcuDetailViewModel,
               onBack:()->Unit
) {

    LaunchedEffect(key1 = "lala") {
        viewModel.initData()
    }
    Scaffold(
        topBar = {
            TopAppBar(

                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                          onBack.invoke()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_action_arrow_left),
                            contentDescription = ""
                        )
                    }
                }
            )
        }

    ) {
        Column {
            viewModel.pageData.banner.observeAsState().value?.apply {
                LiveBannerView(this as JSONObject)
            }
            viewModel.pageData.samples.observeAsState().value?.apply {
                SampleLooper(datas = this)
            }

            viewModel.pageData.options.observeAsState().value?.firstOrNull()?.apply {
                OptionsView(item = this)
            }
        }
    }

}


@ExperimentalPagerApi
@Composable
fun SampleLooper(datas: MutableList<SeriesPcuDetailSampleItem>) {
    val pageState = rememberPagerState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(datas.size) {
            datas!!.forEach { data ->
                Image(
                    painter = rememberGlidePainter(request = data.sampleUrl),
                    contentDescription = "lal",
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .aspectRatio(1.0f)
                )
            }
        }

        HorizontalPagerIndicator(
           pagerState = pageState
        )
    }
}

@Composable
fun OptionsView(item: SeriesPcuCategoryItem) {
    var child by remember {
        mutableStateOf<SeriesPcuCategoryItem?>(null)
    }
    Column {
        Text(item.title ?: "")
        LazyRow() {
            items(item.childs) { it ->
                OptionsItem(item = it, onClick = { c ->
                    child = c
                })
            }
        }
        child?.let {
            Column() {
                OptionsView(item = it)
            }
        }

    }
}


@Composable
fun OptionsItem(item: SeriesPcuCategoryItem, onClick: ((SeriesPcuCategoryItem) -> Unit)? = null) {
    Column(
        Modifier
            .border(1.dp, color = Color.Black)
            .size(120.dp, 80.dp)
            .clickable {
                if (item.childs.size > 0) {
                    onClick?.invoke(item)
                }
            }
    ) {
        Text(
            text = item.childTypeName, modifier =
            Modifier.fillMaxWidth(), textAlign = TextAlign.Center

        )
        Text(text = item.price.toString())
    }

}
































