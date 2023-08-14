package com.rizki.qrispayment.data.datasource.local.entities

import androidx.room.Entity

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

/**
 * All fields bellows are dummy data, you can change it as you want
 */

@Entity(tableName = "BankDeposit")
data class BankDepositLocalEntity(
    val idTransaction: String,
    val nominalMoney: Int = 500_000_000,
    val isToggleDeposit: Boolean = false
)
