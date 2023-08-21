package com.rizki.qrispayment.features.payment_histories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizki.qrispayment.common.components.ApplicationAppbar
import com.rizki.qrispayment.common.components.CircularProgress
import com.rizki.qrispayment.common.components.ErrorComponent
import com.rizki.qrispayment.common.components.LabelAndValue
import com.rizki.qrispayment.common.toCurrencyIDRFormat
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */
@Composable
internal fun PaymentHistoriesScreen(
    uiState: PaymentHistoriesUiState,
    onPopBack: () -> Unit,
) {
    PaymentHistoriesScreen(
        uiState = uiState,
        onPopBack = onPopBack,
        success = { paymentDetail, modifier ->
            PaymentHistoriesItem(
                paymentDetail = paymentDetail,
                modifier = modifier
            )
        },
        failed = { message, modifier ->
            ErrorComponent(message = message ?: "Unknown Error", modifier = modifier)
        }
    )
}

@Composable
fun PaymentHistoriesItem(paymentDetail: PaymentDetailEntity?, modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabelAndValue(label = "Merchant Name", value = paymentDetail?.merchantName ?: "")
            LabelAndValue(
                label = "Amount",
                value = paymentDetail?.totalAmount?.toCurrencyIDRFormat().toString()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PaymentHistoriesScreen(
    uiState: PaymentHistoriesUiState,
    onPopBack: () -> Unit,
    success: @Composable (paymentDetail: PaymentDetailEntity?, modifier: Modifier) -> Unit,
    failed: @Composable (message: String?, modifier: Modifier) -> Unit
) {
    Scaffold(
        topBar = { ApplicationAppbar(title = "Payment Histories", onClickBack = onPopBack) }
    ) {
        val modifier = Modifier.padding(it)
        PaymentHistoriesLoading(
            isLoading = if (uiState is PaymentHistoriesUiState.Loading) uiState.isLoading else false,
            loading = { CircularProgress() }
        ) {
            when (uiState) {
                is PaymentHistoriesUiState.Success -> {
                    Column(
                        modifier = modifier
                            .fillMaxSize(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Payment Histories",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .background(Color.White)
                        ) {
                            if (uiState.data?.size != 0 && uiState.data?.size != null) {
                                items(count = uiState.data.size) { index ->
                                    val paymentHistory = uiState.data[index]
                                    success(paymentHistory, Modifier.fillMaxWidth())
                                }
                            } else {
                                item {
                                    failed("No Payment Histories", modifier = modifier)
                                }
                            }

                        }
                    }

                }

                is PaymentHistoriesUiState.Error -> {
                    failed(uiState.message, modifier)
                }

                else -> {

                    failed(IllegalStateException("Invalid state").message, modifier)
                }
            }
        }

    }
}

@Composable
internal fun PaymentHistoriesLoading(
    isLoading: Boolean,
    loading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    if (isLoading) loading()
    else content()
}
