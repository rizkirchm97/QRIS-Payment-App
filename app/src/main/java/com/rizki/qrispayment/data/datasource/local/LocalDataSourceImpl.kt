package com.rizki.qrispayment.data.datasource.local

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.dao.BankDepositDao
import com.rizki.qrispayment.data.datasource.local.dao.PaymentDetailDao
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
class LocalDataSourceImpl @Inject constructor(
    private val paymentDetailDao: PaymentDetailDao,
    private val bankDepositDao: BankDepositDao,
) : LocalDataSource {

    override suspend fun saveToPaymentDb(paymentLocalEntity: PaymentLocalEntity): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            try {
                paymentDetailDao.insert(paymentLocalEntity)
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override suspend fun saveToBankDepositDb(bankDepositLocalEntity: BankDepositLocalEntity): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            try {
                bankDepositDao.upsert(bankDepositLocalEntity)
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentLocalEntity>> = flow {
        emit(Resource.Loading())
        try {
            val paymentDetail = paymentDetailDao.getPaymentDetail(idTransaction)
            emit(Resource.Success(paymentDetail))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }

    override suspend fun clearPaymentDetail(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            paymentDetailDao.clearPaymentDetail()
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }

    override suspend fun clearBankDeposit(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            bankDepositDao.delete()
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }

    override suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentLocalEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val paymentDetail = paymentDetailDao.getAllPaymentDetail()
            emit(Resource.Success(paymentDetail))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}