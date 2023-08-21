package com.rizki.qrispayment.features.payment_histories

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizki.qrispayment.common.components.ApplicationAppbar
import com.rizki.qrispayment.common.components.CircularProgress

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */
@Composable
internal fun PaymentHistoriesScreen(
    uiState: PaymentHistoriesUiState,
    onPopBack: () -> Unit,
) {
    PayementHistoriesScreen(
        uiState = uiState,
        onPopBack = onPopBack,
        success = {

        },
        failed = {

        }
    )
}



internal fun PayementHistoriesScreen(
    uiState: PaymentHistoriesUiState,
    onPopBack: () -> Unit,
    success: () -> Unit,
    failed: () -> Unit
) {
    Scaffold(
        topBar = { ApplicationAppbar( title = "Payment Histories", onClickBack = onPopBack) }
    ) {
        val modifier = Modifier.padding(it)
        PaymentHistoriesLoading(
            isLoading = if (uiState is PaymentHistoriesUiState.Loading) uiState.isLoading else false,
            loading = { CircularProgress() },
            content = {

                when(uiState) {
                    is PaymentHistoriesUiState.Success -> success()
                    is PaymentHistoriesUiState.Error -> failed()
                }
                LazyColumn(modifier = modifier.fillMaxSize()) {
                    items(count = uiState, itemContent = { index ->
                        val paymentHistory = uiState.paymentHistories[index]
                        PaymentHistoryItem(paymentHistory = paymentHistory)
                    }
                }
            }
        )

    }
}

@Composable
internal fun PaymentHistoriesLoading(
    isLoading: Boolean,
    loading: () -> Unit,
    content: () -> Unit) {

    if (isLoading) loading()
    else content()
}
