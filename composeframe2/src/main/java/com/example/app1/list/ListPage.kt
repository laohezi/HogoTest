package com.example.app1.list

import SeriesPcuCategoryItem
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.app1.Screen
import com.example.app1.utils.getScreenWidth
import com.example.app1.utils.px2dp

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ListPage(
    viewModel: ListViewModel? = null,
    navController: NavHostController,
    scaffoldState: ScaffoldState = rememberScaffoldState(),

) {
    val _viewModel:ListViewModel = viewModel ?: viewModel(modelClass = ListViewModel::class.java)


    var inc by remember {
        mutableStateOf(1)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "FreePrints Frames") },
            )
        },

        content = {

            Column() {
                if (_viewModel.items.size > 0) {
                    LazyColumn(Modifier.fillMaxWidth()) {
                        _viewModel.banner?.let {
                            item {
                                ImageWithLoading(
                                    url = it as String, imageViewModifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(2.0f)
                                )
                            }
                        }
                        items(_viewModel.items.size) { index ->
                            InnerList(item = _viewModel.items[index], onItemClick = {
                                navController.navigate(Screen.DetailScreen.toString(),
                                    )
                            })
                        }
                    }
                }

            }
        }

    )


}


@Composable

fun InnerList(item: SeriesPcuCategoryItem,onItemClick:()->Unit) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Text(text = item.title ?: "")
        if (item.childs.size > 0) {
            LazyRow() {
                itemsIndexed(item.childs) { index, child ->
                    val padding = if (index != 0) {
                        12.dp
                    } else {
                        0.dp
                    }
                    val modifier = Modifier
                        .padding(start = padding)
                        .width(px2dp(getScreenWidth() / 2.6f - 30).dp)
                    PcuItemAdapter(
                        item = child,
                        modifier,
                        onClick = onItemClick
                    )
                }
            }
        }
    }
}


@Composable
fun PcuItemAdapter(item: SeriesPcuCategoryItem, modifier: Modifier, onClick: (() -> Unit)? = null) {
    Column(
        Modifier
            .clickable(enabled = item.isSale()) {
                onClick?.invoke()
            }
            .then(modifier)
    ) {

        item.thumbnails.firstOrNull()?.let {
            Box() {
                ImageWithLoading(
                    url = it, imageViewModifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.0f)
                )
                if (item.isSale().not()){
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88ffffff)))
                }
               
            }
            
            
        }
        var readyToDraw by remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .drawWithContent {
                    if (readyToDraw) {
                        drawContent()
                    }
                }
            ,


            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var scale by remember {
                mutableStateOf(1f)
            }

            Text(text = item.title ?: "", maxLines = 2,
                style = LocalTextStyle.current.copy(
                    fontSize = LocalTextStyle.current.fontSize.times(scale),
                    textAlign = TextAlign.Center
                ),

                modifier = Modifier.drawWithContent {
                      if (readyToDraw){
                          drawContent()
                      }
                },

                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        scale *= 0.95f
                    }else{
                        readyToDraw = true
                    }
                }
            )
            Text(text = item.price?.toString() ?: "")
            item.json?.optString("more_options_caption")?.let {
                Text(
                    text = it, color = Color.Gray, style = TextStyle(
                        fontSize = 12.sp
                    )
                )
            }
        }

    }

}


typealias  NT = (Screen) -> Unit

@Composable
fun ImageWithLoading(url: String, imageViewModifier: Modifier) {
    val painter = rememberImagePainter(url)
    Box {
        Image(
            painter = painter,
            contentDescription = "Image",
            modifier = imageViewModifier,
            contentScale = ContentScale.Crop
        )
        when (painter.state) {
            is ImagePainter.State.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is ImagePainter.State.Error -> {
                println(painter.state)
            }
            else->{

            }
        }

    }

}


