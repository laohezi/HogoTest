package com.example.app1

sealed class PageState {
    object Loading : PageState()
    class Error(val e: Throwable) : PageState()
    object Complete : PageState()
}