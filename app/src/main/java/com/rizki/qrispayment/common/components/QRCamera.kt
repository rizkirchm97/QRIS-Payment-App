package com.rizki.qrispayment.common.components

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
    onError: @Composable (String) -> Unit,
    modifier: Modifier = Modifier
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
        },
            modifier = Modifier
                .fillMaxSize()
        )

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            DrawSquareOverlay()
        }

        if (state is CameraState.Scanned) {
            (state as CameraState.Scanned).data?.let { onSuccess(it) }
        } else if (state is CameraState.Error) {
            (state as CameraState.Error).data?.let { onError(it) }
        }

    }

}



@Composable
fun DrawSquareOverlay() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 5f
        val cornerRadius = 20f // Adjust the corner radius as needed
        val squareSize = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2


        // Draw the rounded square with transparent center
        val transparentColor = Color(0x00000000)
        drawRoundRect(
            color = transparentColor,
            size = Size(squareSize, squareSize),
            topLeft = Offset(centerX - squareSize / 2, centerY - squareSize / 2),
            cornerRadius = CornerRadius(cornerRadius),
            style = Stroke(width = strokeWidth)
        )

        // Draw the rounded square outline
        drawRoundRect(
            color = Color.White,
            size = Size(squareSize, squareSize),
            topLeft = Offset(centerX - squareSize / 2, centerY - squareSize / 2),
            cornerRadius = CornerRadius(cornerRadius),
            style = Stroke(width = strokeWidth)
        )
    }

}


sealed interface CameraState {
    object Initializing : CameraState
    data class Scanned(val data: String?) : CameraState
    data class Error(val data: String?) : CameraState
}
