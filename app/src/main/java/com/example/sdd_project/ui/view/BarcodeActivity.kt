package com.example.sdd_project.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                setContent {
                    MaterialTheme {
                        Surface(color = Color.Black) {
                            CameraView()
                        }
                    }
                }
                cameraExecutor = Executors.newSingleThreadExecutor()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
        {
            setContent {
                MaterialTheme {
                    Surface(color = Color.Black) {
                        CameraView()
                    }
                }
            }
            cameraExecutor = Executors.newSingleThreadExecutor()
        } else {
            requestCameraPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @OptIn(ExperimentalGetImage::class) @Composable
    fun CameraView() {
        var detectedQRCodes by remember { mutableStateOf<List<Barcode>>(emptyList()) }
        val context = LocalContext.current
        val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

        val previewView = remember {
            PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }

        DisposableEffect(cameraProviderFuture) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also { it ->
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        val inputImage = InputImage.fromMediaImage(
                            imageProxy.image!!,
                            rotationDegrees
                        )
                        val barcodeScanner = BarcodeScanning.getClient()
                        barcodeScanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                if (barcodes.isNotEmpty()){
                                    val qrCodes = barcodes.filter {
                                        it.format == Barcode.FORMAT_QR_CODE
                                    }
                                    detectedQRCodes = qrCodes
                                    qrCodes.forEach { qrCode ->
                                        // Do something with the QR code data
                                        Log.d("TAG", "CameraView: ${qrCode.displayValue}")
                                        setContent {
                                            MaterialTheme {
                                                Surface(color = Color.White) {
                                                    BarcodePage(qrCode.displayValue.toString())
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure
                                Log.e("TAG", "Barcode scanning failed: $exception")
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    context as LifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                // Handle exception
                Log.e("TAG", "Use case binding failed", exc)
            }

            onDispose {
                cameraProvider.unbindAll()
            }
        }

        Column {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )

            Spacer(modifier = Modifier.height(16.dp))

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//
//            }
        }
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}
