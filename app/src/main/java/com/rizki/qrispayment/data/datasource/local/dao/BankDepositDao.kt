package com.rizki.qrispayment.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Dao
interface BankDepositDao {

    @Upsert
    suspend fun upsert(paymentLocalEntity: BankDepositLocalEntity)


    @Query("DELETE FROM BankDeposit")
    suspend fun delete()

}
