package com.rizki.qrispayment.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizki.qrispayment.common.components.ApplicationAppBar
import com.rizki.qrispayment.common.components.CircularProgress
import com.rizki.qrispayment.common.toCurrencyIDRFormat
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity

@Composable
internal fun HomePaymentScreen(
    uiState: HomeUiState,
    onTapScanQr: () -> Unit,
    onTapPaymentHistory: () -> Unit
) {
    HomePaymentScreen(
        uiState = uiState,
        success = { data, nominalString, modifier ->
            HomePaymentContentView(
                data = data,
                nominal = nominalString,
                onTapScanQr = onTapScanQr,
                onTapPaymentHistory = onTapPaymentHistory,
                modifier
            )
        },
        failed = { message ->
            AlertDialog(
                onDismissRequest = { },
                title = { },
                text = { },
                confirmButton = { },
                dismissButton = { }
            )
        }
    )
}

@Composable
fun HomePaymentContentView(
    data: BankDepositDetailEntity?,
    nominal: String,
    onTapScanQr: () -> Unit,
    onTapPaymentHistory: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(120.dp)
            .clickable { onTapScanQr() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .height(120.dp)
                .background(color = Color(0xFFE55300), shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Saldo Anda",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = nominal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = "Scan QR",
                        tint = Color.White,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable { onTapScanQr() }
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = "Payment History",
                        tint = Color.White,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .clickable { onTapPaymentHistory() }
                    )
                }
            }


        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomePaymentScreen(
    uiState: HomeUiState,
    success: @Composable (data: BankDepositDetailEntity?, nominalString: String, modifier: Modifier) -> Unit,
    failed: @Composable (message: String) -> Unit
) {
    Scaffold(topBar = { ApplicationAppBar(title = "QRIS Payment") }) {
        val modifier = Modifier.padding(it)
        LoadingScreen(
            uiState,
            loadingComponent = {
                CircularProgress()
            },
            content = {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(color = Color.White)
                ) {
                    when (uiState) {
                        is HomeUiState.Success -> {
                            success(uiState.data, uiState.nominalStirng, Modifier.fillMaxWidth())
                        }

                        is HomeUiState.Error -> {
                            failed(uiState.error)
                        }

                        else -> Unit
                    }
                }

            }
        )
    }
}

@Composable
fun LoadingScreen(
    uiState: HomeUiState,
    loadingComponent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (uiState is HomeUiState.Loading) {
        loadingComponent()
    } else {
        content()
    }
}
