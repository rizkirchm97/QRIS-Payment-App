package com.rizki.qrispayment.features.payment_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rizki.qrispayment.common.NavRoute
import com.rizki.qrispayment.features.payment_detail.PaymentDetailRoute

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */


fun NavController.navigateToHome() {
    this.navigate(NavRoute.HomeScreen.route) {
        popUpTo(NavRoute.PaymentDetailScreen.route) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.paymentNavigation(
    onNavigateToHome: () -> Unit,
) {
    composable(route = "${NavRoute.PaymentDetailScreen.route}/{paymentId}", arguments = listOf(
        navArgument("paymentId") {
            type = NavType.StringType
        }
    )
    ) {
        PaymentDetailRoute(
            onNavigateToHome = onNavigateToHome
        )
    }
}
