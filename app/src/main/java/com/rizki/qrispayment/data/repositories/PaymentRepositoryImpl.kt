package com.rizki.qrispayment.data.repositories

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.LocalDataSource
import com.rizki.qrispayment.data.mapper.mapToData
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : PaymentRepository {
    override suspend fun savePayment(
        paymentDetailEntity: PaymentDetailEntity,
        bankDepositDetailEntity: BankDepositDetailEntity
    ) {

        val getNominal = localDataSource.getBankDepositDetail()
        getNominal.collect { bankData ->
            if (bankData is Resource.Success) {
                if (bankData.data != null) {
                    val nominalDeposit = bankData.data.nominalMoney
                    val totalDeposit = nominalDeposit?.minus(paymentDetailEntity.totalAmount)
                    val bankDeposit = BankDepositDetailEntity(
                        bankId = bankData.data.bankId,
                        nominalMoney = totalDeposit,
                    )
                    localDataSource.saveToBankDepositDb(bankDeposit.mapToData())
                    localDataSource.saveToPaymentDb(paymentDetailEntity.mapToData())
                } else {
                    localDataSource.saveToBankDepositDb(bankDepositDetailEntity.mapToData())
                    localDataSource.saveToPaymentDb(paymentDetailEntity.mapToData())
                }

            }


        }


    }

    override suspend fun getPaymentDetailById(idTransaction: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearPaymentDetail() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllPaymentDetail(): Flow<List<PaymentDetailEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestBankDeposit(): Flow<BankDepositDetailEntity> {
        TODO("Not yet implemented")
    }


}