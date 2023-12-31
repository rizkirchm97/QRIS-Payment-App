package com.rizki.qrispayment.features.scan_qr.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rizki.qrispayment.common.NavRoute
import com.rizki.qrispayment.features.scan_qr.ScanQrRoute

fun NavController.navigateToPaymentDetail(paymentId: String?) {
    this.navigate(NavRoute.PaymentDetailScreen.route+"/$paymentId")
}

fun NavGraphBuilder.scanQrNavigation(
    onPopBack: () -> Unit,
    onNavigateToPayment: (String?) -> Unit,
) {
    composable(route = NavRoute.QrScanScreen.route) {
        ScanQrRoute(
            onPopBack = onPopBack,
            onNavigateToPayment = onNavigateToPayment
        )
    }
}