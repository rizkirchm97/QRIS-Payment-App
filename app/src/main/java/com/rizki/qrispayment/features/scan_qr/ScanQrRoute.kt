package com.rizki.qrispayment.features.scan_qr

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScanQrRoute(
    viewModel: ScanQrViewModel = hiltViewModel(),
    onPopBack: () -> Unit,
    onPaymentTap: () -> Unit,
) {
}