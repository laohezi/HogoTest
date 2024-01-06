package com.example.hogotest.usage

import android.app.usage.UsageStatsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils


class UsageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermission(   android.Manifest.permission.PACKAGE_USAGE_STATS,android.os.Process.myPid(),android.os.Process.myUid())== PackageManager.PERMISSION_GRANTED) {
            getUsage()
        }else{
            val intent: Intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        getUsage()
    }
    //获取用户app使用记录
    fun getUsage(){
        val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
        val startTime = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7
        val endTime = System.currentTimeMillis()
        val usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime)
        val json = GsonUtils.toJson(usageStats)
        // save  the json file into local storage
        val filePath = "$filesDir/usage.json"
        FileUtils.createFileByDeleteOldFile(filePath)
        FileIOUtils.writeFileFromString(filePath,json)

    }


}
