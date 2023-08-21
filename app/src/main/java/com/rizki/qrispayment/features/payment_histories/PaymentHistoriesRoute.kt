package com.rizki.qrispayment.features.payment_histories

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */

@Composable
fun PaymentHistoriesRoute(
    viewModel: PaymentHistoriesViewModel = hiltViewModel(),
    onPopBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PaymentHistoriesScreen(
        uiState = uiState,
        onPopBack = onPopBack,
    )
}