package com.rizki.qrispayment.data.datasource.local

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.dao.BankDepositDao
import com.rizki.qrispayment.data.datasource.local.dao.PaymentDetailDao
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
class LocalDataSourceImpl @Inject constructor(
    private val db: PaymentDatabase,
) : LocalDataSource {

    override suspend fun saveToPaymentDb(paymentLocalEntity: PaymentLocalEntity): Flow<Resource<String>> =
        flow {

            try {
                db.paymentDao.insert(paymentLocalEntity)
                emit(Resource.Success(paymentLocalEntity.id))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }


        }

    override suspend fun saveToBankDepositDb(bankDepositLocalEntity: BankDepositLocalEntity): Flow<Resource<Unit>> =
        flow {

            try {
                db.bankDepositDao.upsert(bankDepositLocalEntity)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }


        }

    override suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentLocalEntity>> =
        flow<Resource<PaymentLocalEntity>> {

            val paymentDetail = db.paymentDao.getPaymentDetail(idTransaction)
            paymentDetail
                .catch { e ->
                    emit(Resource.Error(e.message.toString()))
                }
                .collect { result ->
                    emit(Resource.Success(result))

                }


        }.flowOn(Dispatchers.IO)

    override suspend fun getBankDepositDetail(): Flow<Resource<BankDepositLocalEntity>> =
        flow<Resource<BankDepositLocalEntity>> {
            val bankDepositDetail = db.bankDepositDao.getLatestBankDeposit()
            bankDepositDetail
                .catch { e ->
                    emit(Resource.Error(e.message.toString()))
                }
                .collect { result ->
                    emit(Resource.Success(result))
                }
        }.flowOn(Dispatchers.IO)

    override suspend fun clearPaymentDetail(): Flow<Resource<Unit>> = flow {

        try {
            db.paymentDao.clearPaymentDetail()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }


    }.flowOn(Dispatchers.IO)

    override suspend fun clearBankDeposit(): Flow<Resource<Unit>> = flow {


        try {
            db.bankDepositDao.delete()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }


    }.flowOn(Dispatchers.IO)

    override suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentLocalEntity>>> = flow {

        val paymentDetail = db.paymentDao.getAllPaymentDetail()
        paymentDetail
            .catch { e ->
                emit(Resource.Error(e.message.toString()))
            }
            .collect { result ->
                emit(Resource.Success(result))
            }

    }
}