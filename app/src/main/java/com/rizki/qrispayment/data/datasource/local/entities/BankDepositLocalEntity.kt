package com.rizki.qrispayment.data.datasource.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

/**
 * All fields bellows are dummy data, you can change it as you want
 */

@Entity(tableName = "BankDeposit")
data class BankDepositLocalEntity(
    @PrimaryKey(autoGenerate = false)
    val bankId: String,
    val nominalMoney: Long?,
)
