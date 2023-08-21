package com.rizki.qrispayment.features.payment_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizki.qrispayment.common.components.CircularProgress
import com.rizki.qrispayment.common.components.LabelAndValue
import com.rizki.qrispayment.common.toCurrencyIDRFormat
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */

@Composable
internal fun PaymentDetailScreen(
    uiState: PaymentUiState,
    onNavigateToHome: () -> Unit
) {
    PaymentDetailScreen(
        uiState = uiState,
        success = { paymentDetail, bankDeposit, modifier ->
            PaymentDetailContent(
                paymentDetail = paymentDetail,
                bankDeposit = bankDeposit,
                modifier = modifier,
                onNavigateToHome = onNavigateToHome
            )
        },
        error = { message ->

        }
    )
}

@Composable
fun PaymentDetailContent(
    paymentDetail: PaymentDetailEntity?,
    bankDeposit: BankDepositDetailEntity?,
    modifier: Modifier,
    onNavigateToHome: () -> Unit
) {
    val state = rememberScrollState()
    LaunchedEffect(Unit) {
        state.animateScrollTo(100)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.padding(16.dp)
                .aspectRatio(0.8f, true)
                .clip(shape = MaterialTheme.shapes.medium)
                .shadow(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Text("Payment Detail", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                LabelAndValue(
                    label = "Payment Status",
                    value = "Berhasil"
                )
                LabelAndValue(
                    label = "Transaction Id",
                    value = paymentDetail?.idTransaction ?: ""
                )
                LabelAndValue(
                    label = "Merchant Name",
                    value = paymentDetail?.merchantName ?: ""
                )
                LabelAndValue(
                    label = "Nominal Transaction",
                    value = paymentDetail?.totalAmount?.toCurrencyIDRFormat().toString()
                )
                LabelAndValue(
                    label = "Remaining Balance",
                    value = bankDeposit?.nominalMoney?.toCurrencyIDRFormat().toString() ?: ""
                )


            }


        }
        Box(
            modifier = Modifier
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            TextButton(onClick = { onNavigateToHome() }) {
                Text("Back to Home")
            }
        }
    }




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PaymentDetailScreen(
    uiState: PaymentUiState,
    success: @Composable (
        paymentDetail: PaymentDetailEntity?,
        bankDeposit: BankDepositDetailEntity?,
        modifier: Modifier
    ) -> Unit,
    error: @Composable (message: String) -> Unit,
) {

    Scaffold {
        val modifier = Modifier.padding(it)

        PaymentLoadingScreen(
            isLoading = if (uiState is PaymentUiState.Loading) uiState.isLoading else false,
            loading = { CircularProgress() },
            content = {
                Box(modifier = modifier) {
                    when (uiState) {
                        is PaymentUiState.Success -> {


                            success(
                                uiState.data?.paymentDetailEntity,
                                uiState.data?.bankDepositDetailEntity,
                                Modifier.fillMaxSize()
                            )
                        }

                        is PaymentUiState.Error -> {
                            error(uiState.error)
                        }

                        else -> Unit
                    }
                }
            }
        )
    }

}

@Composable
fun PaymentLoadingScreen(
    isLoading: Boolean,
    loading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (isLoading) loading()
    else content()
}
