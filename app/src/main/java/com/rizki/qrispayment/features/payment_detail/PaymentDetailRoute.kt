package com.rizki.qrispayment.features.payment_detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */
@Composable
fun PaymentDetailRoute(
    viewModel: PaymentDetailViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PaymentDetailScreen(
        uiState = uiState,
        onNavigateToHome = onNavigateToHome
    )
}