package com.rizki.qrispayment.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Dao
interface PaymentDetailDao {
    @Upsert
    suspend fun upsert(paymentLocalEntity: PaymentLocalEntity)

    @Insert
    suspend fun insert(paymentLocalEntity: PaymentLocalEntity)

    @Query("SELECT * FROM PaymentDetail WHERE idTransaction = :idTransaction")
    suspend fun getPaymentDetail(idTransaction: String): Flow<PaymentLocalEntity>

    @Query("DELETE FROM PaymentDetail")
    suspend fun clearPaymentDetail()

    @Query("SELECT * FROM PaymentDetail")
    suspend fun getAllPaymentDetail(): Flow<List<PaymentLocalEntity>>
}