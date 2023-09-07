import android.Manifest
import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ScreenUtils
import com.hugo.mylibrary.R
import java.io.File
import java.io.IOException


/**
 *  跟activity 生命周期绑定的录屏工具类，activity 销毁后，录制自动结束
 *
 *  用法 ： 在 activity 中初始化  val screenRecorder = ScreenRecorder(this)
 *  然后在 activity 的 onCreate 中调用 lifecycle.addObserver(screenRecorder)
 *
 *  checkPermissionAndStartRecording 开始录制
 *
 *
 *  其中 FileUtils 和 ScreenUtils 这俩工具类 来自blankj 大佬的AndroidUtilCode
 *
 * */
class ScreenRecorder(val activity: ComponentActivity) : LifecycleObserver {

    private var screenRecordLauncher: ActivityResultLauncher<Intent>? = null
    var saveFilePath: String? = null
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun bindActivity() {
        permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions.entries.all { it.value == true }) {
                    startScreenRecording()
                } else {

                }

            }
        screenRecordLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    // 获得权限，启动Service开始录制
                    val startRecordIntent = Intent(activity, ScreenRecordService::class.java)
                    startRecordIntent.putExtra("code", result.resultCode)
                    startRecordIntent.putExtra("data", result.data)
                    if (!saveFilePath.isNullOrEmpty()) {
                        startRecordIntent.putExtra("path", saveFilePath)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.startForegroundService(startRecordIntent)
                    } else {
                        activity.startService(startRecordIntent)
                    }
                } else {

                }
            }
    }


    /**
     * 从这个地方开始录制
     * */
    fun checkPermissionAndStartRecording(saveFilePath: String) {
        this.saveFilePath = saveFilePath

        permissionLauncher?.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

    }

    private fun startScreenRecording() {
        if (activity.checkPermission(
                Manifest.permission.RECORD_AUDIO,
                android.os.Process.myPid(),
                android.os.Process.myUid()
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            return
        }


        val mediaProjectionManager =
            activity.getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val permissionIntent = mediaProjectionManager.createScreenCaptureIntent()

        screenRecordLauncher?.launch(permissionIntent)

    }


    /**
     * 结束录制
     *  */
    fun stopScreenRecording(context: Activity) {
        val service = Intent(context, ScreenRecordService::class.java)
        context.stopService(service)


        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun unbindActivity() {
            stopScreenRecording(activity)
            permissionLauncher?.unregister()
            screenRecordLauncher?.unregister()
            permissionLauncher = null
            screenRecordLauncher = null
        }


    }



 /**
  * 版权声明: 该Service 来自网上其他大佬,这里只是借用,由于当时赶项目,忘记记录出处了,
  * 欢迎知道这个Service 出处的小伙伴指出
  *
  * 录屏Service,api 29开始,录屏必须在一个前台Service中进行了,不能直接在Actvity中进行了
  *
  * */
    class ScreenRecordService : Service() {
        private var mScreenWidth = 0
        private var mScreenHeight = 0
        private var mScreenDensity = 0f
        private var mResultCode = 0
        private var mResultData: Intent? = null

        private var mMediaProjection: MediaProjection? = null
        private var mMediaRecorder: MediaRecorder? = null
        private var mVirtualDisplay: VirtualDisplay? = null
        private var saveFilePath =
            "${Environment.getExternalStorageDirectory().absolutePath + File.separator}${File.separator}DCIM${File.separator}Camera${File.separator}${System.currentTimeMillis()}.mp4"

        override fun onCreate() {
            // TODO Auto-generated method stub
            super.onCreate()
            Log.i(TAG, "Service onCreate() is called")
        }

        override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
            // TODO Auto-generated method stub
            Log.i(TAG, "Service onStartCommand() is called")
            createNotificationChannel()
            mResultCode = intent.getIntExtra("code", -1)
            mResultData = intent.getParcelableExtra("data")
            mScreenWidth = intent.getIntExtra("width", ScreenUtils.getAppScreenWidth())
            mScreenHeight = intent.getIntExtra("height", ScreenUtils.getAppScreenHeight())
            mScreenDensity = intent.getFloatExtra("density", ScreenUtils.getScreenDensity())
            intent.getStringExtra("path")?.let {
                saveFilePath = it
            }
            mMediaProjection = createMediaProjection()
            mMediaRecorder = createMediaRecorder()
            mVirtualDisplay =
                createVirtualDisplay()
            mMediaRecorder!!.start()

            return Service.START_NOT_STICKY
        }

        private fun createMediaProjection(): MediaProjection {
            Log.i(TAG, "Create MediaProjection")
            return (getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?)!!.getMediaProjection(
                mResultCode,
                mResultData!!
            )
        }

        private fun createMediaRecorder(): MediaRecorder {


            Log.i(TAG, "Create MediaRecorder")
            val mediaRecorder = MediaRecorder()
             mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            val file = File(saveFilePath)
            if (!file.exists()) {
                //
                FileUtils.createOrExistsFile(file)
            }
            mediaRecorder.setOutputFile(
                file
            )
            mediaRecorder.setVideoSize(
                mScreenWidth,
                mScreenHeight
            ) //after setVideoSource(), setOutFormat()
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.HEVC) //after setOutputFormat()
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC) //after setOutputFormat()
            /// 我这边测试这个比特率设置没什么效果.....也不知道为啥
            mediaRecorder.setVideoEncodingBitRate(mScreenWidth * mScreenHeight)

            mediaRecorder.setVideoFrameRate(20)
            mScreenWidth * mScreenHeight / 1000

            try {
                mediaRecorder.prepare()
            } catch (e: IllegalStateException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return mediaRecorder
        }

        private fun createVirtualDisplay(): VirtualDisplay {
            Log.i(TAG, "Create VirtualDisplay")
            return mMediaProjection!!.createVirtualDisplay(
                TAG,
                mScreenWidth,
                mScreenHeight,
                mScreenDensity.toInt(),
                DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY,
                mMediaRecorder!!.surface,
                null,
                null
            )
        }

        override fun onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy()
            Log.i(TAG, "Service onDestroy")
            if (mVirtualDisplay != null) {
                mVirtualDisplay!!.release()
                mVirtualDisplay = null
            }
            if (mMediaRecorder != null) {
                mMediaRecorder!!.setOnErrorListener(null)
                mMediaProjection!!.stop()
                mMediaRecorder!!.reset()
            }
            if (mMediaProjection != null) {
                mMediaProjection!!.stop()
                mMediaProjection = null
            }
            // 通知系统将录制的视频添加到媒体库
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(File(saveFilePath))
            sendBroadcast(intent)
        }

        override fun onBind(intent: Intent?): IBinder? {
            // TODO Auto-generated method stub
            return null
        }

        companion object {
            private const val TAG = "ScreenRecordingService"
        }

        // 录制视频必须要 startForeground，否则会出现录制闪退的情况
        private fun createNotificationChannel() {
            val builder = Notification.Builder(this.applicationContext) //获取一个Notification构造器
            // Activity::class.java 可自行替换成想要跳转的activity
            val nfIntent = Intent(this,Activity::class.java) //点击后跳转的界面，可以设置跳转数据
            builder.setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    nfIntent,
                    0
                )
            ) // 设置PendingIntent
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.utils_toast_bg
                    )
                ) // 设置下拉列表中的图标(大图标)
                //.setContentTitle("SMI InstantView") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.utils_toast_bg) // 设置状态栏内的小图标
                .setContentText("is running......") // 设置上下文内容
                .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

            /*以下是对Android 8.0的适配*/
            //普通notification适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId("notification_id")
            }
            //前台服务notification适配
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    "notification_id",
                    "notification_name",
                    NotificationManager.IMPORTANCE_LOW
                )
                notificationManager.createNotificationChannel(channel)
            }
            val notification = builder.build() // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND //设置为默认的声音
            startForeground(110, notification)
        }
    }
}

