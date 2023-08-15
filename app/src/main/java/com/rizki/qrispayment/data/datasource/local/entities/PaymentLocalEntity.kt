package com.rizki.qrispayment.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Entity(tableName = "PaymentDetail")
data class PaymentLocalEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val idTransaction: String?,
    val bankId: String?,
    val merchantName: String?,
    val bankSource: String?,
    val totalAmount: Long?,
)
