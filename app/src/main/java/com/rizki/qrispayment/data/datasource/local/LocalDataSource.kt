package com.rizki.qrispayment.data.datasource.local

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
interface LocalDataSource {
    suspend fun saveToPaymentDb(paymentLocalEntity: PaymentLocalEntity): Flow<Resource<Unit>>
    suspend fun saveToBankDepositDb(bankDepositLocalEntity: BankDepositLocalEntity): Flow<Resource<Unit>>
    suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentLocalEntity>>
    suspend fun clearPaymentDetail(): Flow<Resource<Unit>>
    suspend fun clearBankDeposit(): Flow<Resource<Unit>>
    suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentLocalEntity>>>
}