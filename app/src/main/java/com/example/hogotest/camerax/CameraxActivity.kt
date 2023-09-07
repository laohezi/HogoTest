package com.example.hogotest.camerax

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.Executors

class CameraxActivity : AppCompatActivity() {

    val imageAnalysisExecutor  = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    val cameraProviderFuture by lazy {
        ProcessCameraProvider.getInstance(this)
    }

    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        val executor = ContextCompat.getMainExecutor(this)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(this,preview,cameraProvider)
            }, executor
        )
    }

    lateinit var preview:PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(Manifest.permission.CAMERA)
        setContent {
          SimpleCameraPreview()
        }

    }

    @Composable
    fun SimpleCameraPreview() {
        val lifecycleOwner = LocalLifecycleOwner.current

        val context = LocalContext.current as  ComponentActivity

        AndroidView(factory = { context  ->
            preview = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
            preview

        })

    }

    fun bindPreview(
        lifecycleOwner: LifecycleOwner,

        previewView: PreviewView,
        cameraProvider: ProcessCameraProvider
    ) {
        val cameraController = LifecycleCameraController(previewView.context)
        cameraController.bindToLifecycle(lifecycleOwner)
        previewView.controller = cameraController
        cameraController.isTapToFocusEnabled = true
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        val imageAnalysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
        val barcodeScanner = BarcodeScanning.getClient(options)


        val myAnalyzer = object :ImageAnalysis.Analyzer{
            override fun analyze(image: ImageProxy) {
                Thread.sleep(1000)
                Log.d("检测了结果",image.toString())
                image.close()
            }

        }



        val  analyzer = MlKitAnalyzer(
            listOf(barcodeScanner),
            CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
            ContextCompat.getMainExecutor(this)


        ){  result ->
            val jj = result.getValue(barcodeScanner)?.lastOrNull()
            println("检测到结果是"+jj?.rawValue)
        }
      //  val

      //  cameraController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(this),analyzer)

        cameraController.setImageAnalysisAnalyzer(imageAnalysisExecutor,myAnalyzer)
        cameraController.imageAnalysisImageQueueDepth = 2

        cameraController.imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_BLOCK_PRODUCER






    }

}


fun ImageProxy.toMyString(): String {

    return "${this.format}--${this.imageInfo}"

}