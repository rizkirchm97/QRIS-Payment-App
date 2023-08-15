package com.rizki.qrispayment.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rizki.qrispayment.data.datasource.local.dao.BankDepositDao
import com.rizki.qrispayment.data.datasource.local.dao.PaymentDetailDao
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Database(
    entities = [
        PaymentLocalEntity::class,
        BankDepositLocalEntity::class
    ], version = 2
)
abstract class PaymentDatabase : RoomDatabase() {
    abstract val paymentDao: PaymentDetailDao
    abstract val bankDepositDao: BankDepositDao
}