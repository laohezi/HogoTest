package com.example.app1.detail

import SeriesPcuCategoryItem
import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app1.LiveBannerView
import com.example.app1.NavigationViewModel
import com.example.app1.R
import com.example.app1.Screen
import com.google.accompanist.glide.rememberGlidePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator

import org.json.JSONObject


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")

@Composable
fun DetailPage(
    viewModel: SeriesPcuDetailViewModel,
    onBack: () -> Unit
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

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            viewModel.pageData.banner.observeAsState().value?.apply {
                LiveBannerView(this as JSONObject)
            }
            viewModel.pageData.samples.observeAsState().value?.apply {
                if (this.isNotEmpty()) {
                    SampleLooper(datas = this)
                }
            }

            viewModel.pageData.options.observeAsState().value?.firstOrNull()?.apply {
                OptionsView(item = this)
            }

        }
    }

}



@Composable
fun SampleLooper(datas: MutableList<SeriesPcuDetailSampleItem>) {
    val pageState = rememberPagerState(initialPage = 0, pageCount = { datas.size })
        HorizontalPager(pageState) {
            val data = datas[it]
                Column {
                    Image(
                        painter = rememberGlidePainter(request = data.sampleUrl),
                        contentDescription = "lal",
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .aspectRatio(1.0f)
                    )
                    Text(text = data.sampleUrl)
                }

    }
}

@Composable
fun OptionsView(item: SeriesPcuCategoryItem) {
    var child by remember {
        mutableStateOf<SeriesPcuCategoryItem?>(null)
    }
    var selectPosition by remember {
        mutableStateOf(-1)
    }
    Column {
        Text(item.title ?: "")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(item.childs.size) { index ->
                val sn = item.childs[index].apply {
                    selected = (index == selectPosition)
                    println(selected)
                }

                OptionsItem(
                    item = sn,
                    onClick = { c ->
                        selectPosition = index
                        if (c.childs.isNotEmpty()) {
                            child = c
                        }
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
    Surface(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            1.dp,
            color = if (item.selected) MaterialTheme.colors.primary else Color.Gray
        )
    ) {
        Column(
            modifier = Modifier

                //   .border(1.dp, color = Color.Black)
                .size(120.dp, 80.dp)
                .clickable {

                    onClick?.invoke(item)

                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,

            ) {
            Text(
                text = item.childTypeName, modifier =
                Modifier.fillMaxWidth(), textAlign = TextAlign.Center

            )
            Text(
                text = item.price.toString(), modifier =
                Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }


}


@Preview
@Composable
fun ItemPreview() {
    OptionsItem(item = SeriesPcuCategoryItem(
    ).apply {
        childTypeName = "Size"
        price = "$666"

    })
}































