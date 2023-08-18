package com.rizki.qrispayment.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Dao
interface BankDepositDao {

    @Transaction
    @Upsert
    suspend fun upsert(paymentLocalEntity: BankDepositLocalEntity)


    @Query("DELETE FROM BankDeposit")
    suspend fun delete()

    @Query("SELECT * FROM BankDeposit ORDER BY bankId DESC LIMIT 1")
    fun getLatestBankDeposit(): Flow<BankDepositLocalEntity>

}
