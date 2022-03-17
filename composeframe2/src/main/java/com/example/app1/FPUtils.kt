package com.example.app1

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import androidx.compose.ui.unit.TextUnit

const val SCREEN_SCALE_16_9 = "16/9"
const val SCREEN_SCALE_16_10 = "16/10"

fun checkDeviceType(application: Application?): DeviceType {
    if (application == null) {
        return DeviceType.PHONE_3_5
    }
    val screenSize: Double =
        getTabletScreenSize(
            application
        )
    if ((isPossibleTabletDevice(
            application
        ) || java.lang.Double.compare(screenSize, 6.0) >= 0)
        && java.lang.Float.compare(
            getScreenRatio(
                application
            ), 4f / 3f
        ) == 0
    ) {
        return DeviceType.PAD_3x4
    }
    if (java.lang.Float.compare(
            getScreenRatio(
                application
            ), 16f / 9f
        ) > 0
    ) {
        return DeviceType.PHONE_1x2
    }
    if (java.lang.Float.compare(
            getScreenRatio(
                application
            ), 16f / 9f
        ) <= 0
        && java.lang.Float.compare(
            getScreenRatio(
                application
            ), 16f / 10f
        ) > 0
    ) {
        val type: DeviceType = DeviceType.DEFAULT
        type.screenScale = SCREEN_SCALE_16_9
        return type
    }
    if (java.lang.Float.compare(
            getScreenRatio(application), 16f / 10f
        ) == 0
    ) {
        val type: DeviceType = DeviceType.DEFAULT
        type.screenScale = SCREEN_SCALE_16_10
        return type
    }
    return DeviceType.PHONE_3_5
}

enum class DeviceType {
    PAD_3x4, PHONE_3_5, PHONE_1x2, DEFAULT;

    var screenScale: String? = null
}

fun getTabletScreenSize(application: Application): Double {
    var size = 0.0
    try {

        // Compute screen size
        val dm = application.applicationContext.resources.displayMetrics
        val screenWidth = dm.widthPixels / dm.xdpi
        val screenHeight = dm.heightPixels / dm.ydpi
        size = Math.sqrt(
            Math.pow(screenWidth.toDouble(), 2.0) + Math.pow(
                screenHeight.toDouble(),
                2.0
            )
        )
    } catch (t: Throwable) {
    }
    return size
}

fun isPossibleTabletDevice(application: Application): Boolean {
    try {
        val manager =
            application.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (manager.phoneType == TelephonyManager.PHONE_TYPE_NONE) {
            true
        } else application.applicationContext.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                application.applicationContext.resources
                    .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
    } catch (t: Throwable) {
    }

    // No, this is not a tablet!
    return false
}

fun getSizeAfterScale(originSize:Double): Double {
    return getRealScreenSize(appContext).width.toDouble().div(getMockUpSizeByDevice(checkDeviceType(appContext))).times(originSize)
}

fun getScreenRatio(application: Application): Float {
    val wm = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val realWidth: Float
    val realHeight: Float

    //new pleasant way to get real metrics
    val realMetrics = DisplayMetrics()
    display.getRealMetrics(realMetrics)
    realWidth = realMetrics.widthPixels.toFloat()
    realHeight = realMetrics.heightPixels.toFloat()
    return if (realWidth > realHeight) {
        realWidth / realHeight
    } else realHeight / realWidth
}

fun getRealScreenSize(application: Application): Size {
    val wm = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val dm = application.resources.displayMetrics
    //new pleasant way to get real metrics
    val realMetrics = DisplayMetrics()
    display.getRealMetrics(realMetrics)
    val realWidth = realMetrics.widthPixels
    val realHeight = realMetrics.heightPixels
    return Size(realWidth, realHeight)
}
fun getMockUpSizeByDevice(deviceType: DeviceType): Int {
    return when(deviceType){
        DeviceType.PAD_3x4 -> 1536
        DeviceType.PHONE_3_5 -> 1080
        DeviceType.PHONE_1x2 -> 1440
        DeviceType.DEFAULT -> 1080
    }

}

