package com.rizki.qrispayment.features.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizki.qrispayment.common.components.ApplicationAppBar
import com.rizki.qrispayment.common.components.CircularProgress
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity

@Composable
internal fun HomePaymentScreen() {
    HomePaymentScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomePaymentScreen(
    success: @Composable (data: BankDepositDetailEntity, modifier: Modifier) -> Unit,
    error: @Composable (message: String) -> Unit
) {
    Scaffold(topBar = { ApplicationAppBar(title = "QRIS Payment")}) {
        val modifier = Modifier.padding(it)
        LoadingScreen(
            true,
            loadingComponent = {
                CircularProgress()
            },
            content = {

            }
        )
    }
}

@Composable
fun LoadingScreen(
    uiState: Boolean,
    loadingComponent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (uiState) {
        loadingComponent()
    } else {
        content()
    }
}
