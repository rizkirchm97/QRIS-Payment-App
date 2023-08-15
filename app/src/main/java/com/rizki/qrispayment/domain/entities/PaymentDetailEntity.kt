package com.rizki.qrispayment.domain.entities

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
data class PaymentDetailEntity(
    val id: String,
    val idTransaction: String,
    val bankId: String,
    val merchantName: String,
    val bankSource: String,
    val totalAmount: Long,
)
