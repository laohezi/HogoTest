package com.hugo.mylibrary.utils

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import com.hugo.mylibrary.components.application

fun getAllActivities(): List<ActivityInfo> {
    val packageManager = application.packageManager
    var list = listOf<ActivityInfo>()
    try {
        val packageInfo = packageManager.getPackageInfo(application.packageName, PackageManager.GET_ACTIVITIES)
        list = packageInfo.activities.toList()

    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return list
}