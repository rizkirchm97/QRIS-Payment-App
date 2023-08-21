package com.rizki.qrispayment.features.scan_qr

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.rizki.qrispayment.common.components.ApplicationAppbar
import com.rizki.qrispayment.common.components.CircularProgress
import com.rizki.qrispayment.common.components.QRAlertDialog
import com.rizki.qrispayment.common.components.QRCamera

@Composable
internal fun ScanQrScreen(
    uiState: ScanQrState,
    onPopBack: () -> Unit,
    onNavigateToPayment: (String?) -> Unit,
    onQRSubmitTap: (String) -> Unit,
) {
    ScanQrScreen(uiState = uiState,
        onPopBack = onPopBack,
        onQRSubmitTap = onQRSubmitTap,
        success = { id, modifier ->
            if (id?.isNotBlank() == true) {
                onNavigateToPayment(id)

            }
        },
        failed = { message ->

        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQrScreen(
    uiState: ScanQrState,
    onPopBack: () -> Unit,
    onQRSubmitTap: (String) -> Unit,
    success: @Composable (id: String?, modifier: Modifier) -> Unit,
    failed: @Composable (message: String) -> Unit,
) {

    val showDialog = remember { mutableStateOf(false) }
    val scannedString = remember { mutableStateOf("") }

    val scanningEnabled = remember { mutableStateOf(true) }

    Scaffold(topBar = {
        ApplicationAppbar(title = "Scan QR", onClickBack = onPopBack)
    }) {
        val modifier = Modifier.padding(it)


        Box(modifier = modifier) {
            when (uiState) {
                is ScanQrState.Success -> {
                    success(uiState.data, modifier)
                }

                is ScanQrState.Error -> {
                    scanningEnabled.value = true
                    showDialog.value = true
                    uiState.message?.let { it1 -> failed(it1) }

                }

                is ScanQrState.Loading -> {
                    scanningEnabled.value = false
                    showDialog.value = false
                    CircularProgress()
                }

                is ScanQrState.Idle -> {
                    scanningEnabled.value = true
                    showDialog.value = false
                }
            }
        }


        Box(modifier = modifier) {


            QRCamera(isEnabled = scanningEnabled.value, onSuccess = { value ->
                showDialog.value = true
                scannedString.value = value
            }, onError = {
                failed("Error")
                scanningEnabled.value = true
            })


            Box(modifier = modifier) {
                if (showDialog.value) {
                    QRAlertDialog(title = "Transaction Detail",
                        message = scannedString.value,
                        onConfirm = {

                            showDialog.value = false

                            onQRSubmitTap(scannedString.value)
                        },
                        onDismiss = {
                            showDialog.value = false
                        })
                }
            }


        }


    }
}