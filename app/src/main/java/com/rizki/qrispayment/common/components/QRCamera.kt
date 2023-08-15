package com.rizki.qrispayment.common.components

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner

import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.rizki.qrispayment.common.utils.QrCodeAnalyzer
import com.rizki.qrispayment.common.utils.QrCodeAnalyzerState
import android.util.Size as CameraSize

@Composable
fun QRCamera(
    onSuccess: @Composable (String) -> Unit,
    onError: @Composable (String) -> Unit
) {

    var state by remember {
        mutableStateOf<CameraState>(CameraState.Initializing)
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted

        }
    )

    LaunchedEffect(key1 = true) {

        launcher.launch(android.Manifest.permission.CAMERA)

    }

    if (hasCameraPermission) {
        AndroidView(factory = { ctx ->
            val previewView = PreviewView(ctx)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(CameraSize(previewView.width, previewView.height))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(ctx),
                QrCodeAnalyzer { qrCode ->
                    state = when (qrCode) {
                        is QrCodeAnalyzerState.Scanned -> {
                            CameraState.Scanned(qrCode.result)
                        }

                        is QrCodeAnalyzerState.Error -> {
                            CameraState.Error(qrCode.message)
                        }
                    }
                }
            )

            try {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            previewView
        }

        )
        if (state is CameraState.Scanned) {
            (state as CameraState.Scanned).data?.let { onSuccess(it) }
        } else if (state is CameraState.Error) {
            (state as CameraState.Error).data?.let { onError(it) }
        }

    }

}

sealed interface CameraState {
    object Initializing : CameraState
    data class Scanned(val data: String?) : CameraState
    data class Error(val data: String?) : CameraState
}
