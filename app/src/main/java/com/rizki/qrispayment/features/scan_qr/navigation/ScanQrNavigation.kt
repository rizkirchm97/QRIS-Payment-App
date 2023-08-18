package com.rizki.qrispayment.features.scan_qr.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rizki.qrispayment.common.NavRoute
import com.rizki.qrispayment.features.scan_qr.ScanQrRoute

fun NavGraphBuilder.scanQrNavigation(
    onPopBack: () -> Unit,
    onPaymentTap: () -> Unit,
) {
    composable(route = NavRoute.QrScanScreen.route) {
        ScanQrRoute(
            onPopBack = onPopBack,
            onPaymentTap = onPaymentTap,
        )
    }
}