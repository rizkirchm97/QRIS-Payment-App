package com.rizki.qrispayment.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Dao
interface PaymentDetailDao {
    @Transaction
    @Upsert
    suspend fun upsert(paymentLocalEntity: PaymentLocalEntity)

    @Transaction
    @Insert
    suspend fun insert(paymentLocalEntity: PaymentLocalEntity)

    @Query("SELECT * FROM PaymentDetail WHERE id = :id")
    fun getPaymentDetail(id: String): Flow<PaymentLocalEntity>

    @Query("DELETE FROM PaymentDetail")
    suspend fun clearPaymentDetail()

    @Query("SELECT * FROM PaymentDetail")
    fun getAllPaymentDetail(): Flow<List<PaymentLocalEntity>>
}