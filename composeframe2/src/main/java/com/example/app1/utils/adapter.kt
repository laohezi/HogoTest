package com.example.app1.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.example.app1.appContext

fun px2dp(pxValue: Float): Float {
    val r: Resources = appContext.getResources()
    val scale = r.displayMetrics.density
    return pxValue / scale
}

fun sp2px(spValue: Float): Int {
    val fontScale: Float = appContext.getResources().getDisplayMetrics().scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

fun px2sp(pxValue: Float): Float {
    val fontScale: Float = appContext.getResources().getDisplayMetrics().scaledDensity
    return pxValue / fontScale
}

fun getScreenWidth(): Int {
   return (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager ).defaultDisplay.width
}

