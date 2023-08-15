package com.rizki.qrispayment.domain.repositories

import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
interface PaymentRepository {
    suspend fun savePayment(paymentDetailEntity: PaymentDetailEntity, bankDepositDetailEntity: BankDepositDetailEntity)
    suspend fun getPaymentDetailById(idTransaction: String)
    suspend fun clearPaymentDetail()
    suspend fun getAllPaymentDetail(): Flow<List<PaymentDetailEntity>>
    suspend fun getLatestBankDeposit(): Flow<BankDepositDetailEntity>
}