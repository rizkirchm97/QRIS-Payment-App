package com.rizki.qrispayment.common.utils

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.rizki.qrispayment.common.toByteArray

class QrCodeAnalyzer(
    private val onQrScanned: (state: QrCodeAnalyzerState) -> Unit
): ImageAnalysis.Analyzer {

    private val supportedImageFormats = listOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888,
        ImageFormat.NV21,
        ImageFormat.NV16
    )

    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
            val bytes = image.planes.first().buffer.toByteArray()
            val source = PlanarYUVLuminanceSource(
                bytes,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )

            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

            try {
                val result = MultiFormatReader().apply {
                    setHints(
                        mapOf(
                            DecodeHintType.POSSIBLE_FORMATS to arrayListOf(
                                BarcodeFormat.QR_CODE
                            )
                        )
                    )
                }.decode(binaryBitmap)
                onQrScanned(QrCodeAnalyzerState.Scanned(result.text))
            } catch (e: Exception) {
                onQrScanned(QrCodeAnalyzerState.Error(e.message))
                e.printStackTrace()
            } finally {
                image.close()
            }
        } else {
            onQrScanned(QrCodeAnalyzerState.Error("Format not supported"))
            image.close()
        }
    }


}

sealed interface QrCodeAnalyzerState {
    data class Scanned(val result: String?): QrCodeAnalyzerState
    data class Error(val message: String?): QrCodeAnalyzerState
}