package com.rizki.qrispayment.domain.repositories

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
interface PaymentRepository {
    suspend fun savePayment(paymentDetail: String?): Flow<Resource<String>>
    suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentDetailEntity>>
    suspend fun clearPaymentDetail(): Flow<Resource<Unit>>
    suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentDetailEntity>>>
    suspend fun getLatestBankDeposit(): Flow<Resource<BankDepositDetailEntity>>
}