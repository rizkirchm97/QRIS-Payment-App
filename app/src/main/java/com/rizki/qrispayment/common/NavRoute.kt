package com.rizki.qrispayment.common

sealed class NavRoute(var route: String) {
    object HomeScreen : NavRoute("homeScreen")
    object QrScanScreen : NavRoute("qrScanScreen")
    object PaymentHistoryScreen : NavRoute("paymentHistoryScreen")
    object PaymentDetailScreen : NavRoute("paymentDetailScreen")
}