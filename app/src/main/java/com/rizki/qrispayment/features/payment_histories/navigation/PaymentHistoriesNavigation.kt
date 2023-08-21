package com.rizki.qrispayment.features.payment_histories.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rizki.qrispayment.common.NavRoute
import com.rizki.qrispayment.features.payment_histories.PaymentHistoriesRoute

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */

fun NavGraphBuilder.paymentHistoriesNavigation(
    onPopBack: () -> Unit
) {
    composable(NavRoute.PaymentHistoryScreen.route) {
        PaymentHistoriesRoute(
            onPopBack = onPopBack
        )
    }
}