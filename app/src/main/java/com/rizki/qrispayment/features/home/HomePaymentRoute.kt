package com.rizki.qrispayment.features.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomePaymentRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onTapScanQr: () -> Unit,
    onTapPaymentHistory: () -> Unit
) {

    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    HomePaymentScreen(
        uiState = uiState,
        onTapScanQr = onTapScanQr,
        onTapPaymentHistory = onTapPaymentHistory
    )
}