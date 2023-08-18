package com.rizki.qrispayment.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.rizki.qrispayment.features.home.navigation.homeNavigation
import com.rizki.qrispayment.features.home.navigation.navigateToPaymentHistory
import com.rizki.qrispayment.features.home.navigation.navigateToQrScan

@Composable
fun AppNav(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = NavRoute.HomeScreen.route) {
        homeNavigation(
            onTapScanQr = {
                navController.navigateToQrScan()
            },
            onTapPaymentHistory = {
                navController.navigateToPaymentHistory()
            }
        )
    }
}