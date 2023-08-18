package com.rizki.qrispayment.features.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rizki.qrispayment.common.NavRoute
import com.rizki.qrispayment.features.home.HomePaymentRoute

fun NavController.navigateToQrScan() {
    this.navigate(NavRoute.QrScanScreen.route)
}

fun NavController.navigateToPaymentHistory() {
    this.navigate(NavRoute.PaymentHistoryScreen.route)
}

fun NavGraphBuilder.homeNavigation(
    onTapScanQr: () -> Unit,
    onTapPaymentHistory: () -> Unit) {
    composable(route = NavRoute.HomeScreen.route) {
        HomePaymentRoute(
            onTapScanQr = onTapScanQr, onTapPaymentHistory = onTapPaymentHistory
        )
    }
}