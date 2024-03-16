package com.example.app1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph
import com.example.app1.detail.DetailPage
import com.example.app1.list.ListPage
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
               NavGraph()
            }
        }
    }

}

/*@ExperimentalPagerApi
@Composable
fun AppContent() {

    val navigationViewModel = viewModel(modelClass = NavigationViewModel::class.java)
    Crossfade(
        navigationViewModel.current,
    ) { screen ->
        Surface(color = MaterialTheme.colors.background) {
            *//*when (screen) {
                Screen.DetailScreen -> DetailPage()
                Screen.ListScreen -> ListPage()
            }*//*
        }

    }
}*/










