package com.rizki.qrispayment.features.scan_qr

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ScanQrRoute(
    viewModel: ScanQrViewModel = hiltViewModel(),
    onPopBack: () -> Unit,
    onNavigateToPayment: (String?) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScanQrScreen(
        uiState = uiState,
        onPopBack = onPopBack,
        onNavigateToPayment = onNavigateToPayment ,
        onQRSubmitTap = { scannedValue ->
            Log.e("ScanQRValue", "scannedValue: $scannedValue")
            viewModel.onEvent( ScanQREvent.SavePayment(scannedValue)) },
    )
}