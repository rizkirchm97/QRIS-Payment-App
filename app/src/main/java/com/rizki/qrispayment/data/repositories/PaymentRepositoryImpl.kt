package com.rizki.qrispayment.data.repositories

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.LocalDataSource
import com.rizki.qrispayment.data.mapper.mapToData
import com.rizki.qrispayment.data.mapper.mapToDomain
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : PaymentRepository {
    override suspend fun savePayment(
        paymentDetailEntity: PaymentDetailEntity, bankDepositDetailEntity: BankDepositDetailEntity
    ): Flow<Resource<Unit>> = flow {

        val getNominal = localDataSource.getBankDepositDetail()
        getNominal
            .catch { e ->
                emit(Resource.Error(e.message.toString())) }
            .collect { bankData ->
            when (bankData) {
                is Resource.Success -> {
                    if (bankData.data != null) {
                        val nominalDeposit = bankData.data.nominalMoney
                        val totalDeposit = nominalDeposit?.minus(paymentDetailEntity.totalAmount)
                        val bankDeposit = BankDepositDetailEntity(
                            bankId = bankData.data.bankId,
                            nominalMoney = totalDeposit,
                        )
                        val flowBank = localDataSource.saveToBankDepositDb(bankDeposit.mapToData())

                        val flowPayment =  localDataSource.saveToPaymentDb(paymentDetailEntity.mapToData())

                        flowBank
                            .catch { e ->
                                emit(Resource.Error(e.message.toString()))
                            }
                            .combine(flowPayment)
                            { bank, payment ->

                            when {
                                bank is Resource.Success && payment is Resource.Success -> {
                                    emit(Resource.Success(Unit))
                                }
                                bank is Resource.Error || payment is Resource.Error -> {
                                    emit(Resource.Error("Error when save to db"))
                                }
                                bank is Resource.Loading || payment is Resource.Loading -> {
                                    emit(Resource.Loading())
                                }
                            }

                        }

                    } else {
                        val flowBank = localDataSource.saveToBankDepositDb(bankDepositDetailEntity.mapToData())
                        flowBank.collect {result ->
                            if (result is Resource.Success) {

                            }

                        }
                    }

                }

                is Resource.Error -> {
                    emit(Resource.Error(bankData.message + " When get nominal deposit"))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }


            }


        }


    }

    override suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentDetailEntity>> =
        flow {
            val resultData = localDataSource.getPaymentDetailById(idTransaction)

            resultData
                .catch { e ->
                    emit(Resource.Error(e.message.toString())) }
                .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        if (result.data != null) {
                            emit(Resource.Success(result.data.mapToDomain()))
                        }
                    }

                    is Resource.Error -> {
                        emit(Resource.Error(result.message))
                    }

                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }

                }

            }
        }

    override suspend fun clearPaymentDetail(): Flow<Resource<Unit>> =
        localDataSource.clearPaymentDetail()

    override suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentDetailEntity>>> = flow {
        val resultData = localDataSource.getAllPaymentDetail()

        resultData
            .catch { e ->
                emit(Resource.Error(e.message.toString())) }
            .collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        emit(Resource.Success(result.data.map { it.mapToDomain() }))
                    }
                }

                is Resource.Error -> {
                    emit(Resource.Error(result.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }

            }

        }
    }

    override suspend fun getLatestBankDeposit(): Flow<Resource<BankDepositDetailEntity>> = flow {
        val resultData = localDataSource.getBankDepositDetail()

        resultData
            .catch { e ->
                emit(Resource.Error(e.message.toString())) }
            .collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data != null) {
                        emit(Resource.Success(result.data.mapToDomain()))
                    }
                }

                is Resource.Error -> {
                    emit(Resource.Error(result.message))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }

            }

        }
    }


}