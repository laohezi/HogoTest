package com.example.app1

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app1.detail.DetailPage
import com.example.app1.detail.SeriesPcuDetailViewModel
import com.example.app1.list.ListPage
import com.example.app1.list.ListViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

val SCRREN_NAME = "screenName"

class NavigationViewModel() : ViewModel() {
    /* var current by savedStateHandle.getMutableStateOf<Screen>(SCRREN_NAME, Screen.ListScreen,
         save = { it.toBundle() },
         restore = {it.toScreen()}
     )*/
    var current by mutableStateOf<Screen>(Screen.ListScreen)

    fun naviTo(screen: Screen) {
        current = screen
    }

    fun onBack(): Boolean {
        val handled = current !is Screen.ListScreen
        current = Screen.ListScreen
        return handled
    }

}

sealed class Screen(val name: String) {
    object ListScreen : Screen("LIST")
    object DetailScreen : Screen("DETAIL")
}

fun Screen.toBundle(): Bundle {
    return bundleOf(SCRREN_NAME to name)
}

fun Bundle.toScreen(): Screen {
    val name = getString(SCRREN_NAME)
    return when (name) {
        "LIST" -> Screen.ListScreen
        "DETAIL" -> Screen.DetailScreen
        else -> Screen.ListScreen
    }


}


fun <T> SavedStateHandle.getMutableStateOf(
    key: String,
    default: T,
    save: (T) -> Bundle,
    restore: (Bundle) -> T
): MutableState<T> {
    val bundle: Bundle? = get(key)
    val initial = if (bundle == null) {
        default
    } else {
        restore.invoke(bundle)
    }
    val state = mutableStateOf(initial)
    setSavedStateProvider(key) {
        save(state.value)
    }
    return mutableStateOf(initial)
}


@ExperimentalPagerApi
@Composable
fun NavGraph(
    navHostController: NavHostController = rememberNavController(),
    start: String = Screen.ListScreen.toString(),
) {

    NavHost(navController = navHostController, startDestination = start) {
        composable(Screen.ListScreen.toString()) {
            ListRoute(viewModel = viewModel(modelClass = ListViewModel::class.java),navHostController)
        }
        composable(Screen.DetailScreen.toString()) {
            DetailRoute(viewModel = viewModel(modelClass = SeriesPcuDetailViewModel::class.java),onBack = {navHostController.popBackStack()})
        }
    }


}

@Composable
fun ListRoute(
    viewModel: ListViewModel,
    navHostController: NavHostController
) {
    ListPage(viewModel,navHostController)
}

@ExperimentalPagerApi
@Composable
fun DetailRoute(
    viewModel: SeriesPcuDetailViewModel,
    onBack: () -> Unit
) {
    DetailPage(viewModel, onBack)
}
