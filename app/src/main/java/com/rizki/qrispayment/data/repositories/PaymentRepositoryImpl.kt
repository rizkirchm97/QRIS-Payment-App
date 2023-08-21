package com.rizki.qrispayment.data.repositories

import android.util.Log
import com.rizki.qrispayment.common.getCombineBNIUUID
import com.rizki.qrispayment.common.splitQrCode
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : PaymentRepository {
    override suspend fun savePayment(
        paymentDetail: String?
    ): Flow<Resource<String>> = flow {


        emit(Resource.Loading())
        val getNominal = localDataSource.getBankDepositDetail().firstOrNull()

        if (getNominal == null) {
            val bankDepositDetailEntity = BankDepositDetailEntity(
                bankId = "BNI64",
                nominalMoney = 2500000
            )
            val flowBank = localDataSource.saveToBankDepositDb(bankDepositDetailEntity.mapToData())

            flowBank.collect { bankResult ->
                when (bankResult) {
                    is Resource.Success -> {
                        savePayment(paymentDetail)
                    }

                    is Resource.Error -> {
                        emit(Resource.Error("Error when save to db"))
                    }

                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }
        } else {
            if (getNominal is Resource.Success) {
                val id = getCombineBNIUUID()
                val paymentStringDetail = paymentDetail?.splitQrCode()
                val paymentDetailEntity = PaymentDetailEntity(
                    id = id,
                    bankId = getNominal.data?.bankId,
                    bankSource = paymentStringDetail?.get(0),
                    idTransaction = paymentStringDetail?.get(1),
                    merchantName = paymentStringDetail?.get(2),
                    totalAmount = paymentStringDetail?.get(3)?.toLong(),

                    )
                if (getNominal.data?.nominalMoney!! < paymentDetailEntity.totalAmount!!) {
                    emit(Resource.Error("Saldo anda tidak cukup"))
                } else {
                    val nominalDeposit = getNominal.data.nominalMoney
                    val totalDeposit = nominalDeposit.minus(paymentDetailEntity.totalAmount)
                    val bankDeposit = BankDepositDetailEntity(
                        bankId = getNominal.data.bankId,
                        nominalMoney = totalDeposit,
                    )
                    val flowBank = localDataSource.saveToBankDepositDb(bankDeposit.mapToData())

                    val flowPayment = localDataSource.saveToPaymentDb(paymentDetailEntity.mapToData())

                    println(flowPayment.toString())

                    flowBank
                        .catch { e ->
                            emit(Resource.Error(e.message.toString()))
                        }
                        .combine(flowPayment)
                        { bank, payment ->

                            when {
                                bank is Resource.Success && payment is Resource.Success -> {
                                    emit(Resource.Success(payment.data))
                                }

                                bank is Resource.Error && payment is Resource.Error -> {
                                    emit(Resource.Error("Error when save to db"))
                                }

                                bank is Resource.Loading && payment is Resource.Loading -> {
                                    emit(Resource.Loading())
                                }
                            }

                        }.collect()
                }
            }

            if (getNominal is Resource.Error) {
                emit(Resource.Error("Error when save to db"))
            }

        }


    }

    override suspend fun getPaymentDetailById(idTransaction: String): Flow<Resource<PaymentDetailEntity>> =
        flow {
            emit(Resource.Loading())
            val resultData = localDataSource.getPaymentDetailById(idTransaction)

            resultData
                .catch { e ->
                    emit(Resource.Error(e.message.toString()))
                }
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

                        else -> Unit

                    }

                }
        }

    override suspend fun clearPaymentDetail(): Flow<Resource<Unit>> =
        localDataSource.clearPaymentDetail()

    override suspend fun getAllPaymentDetail(): Flow<Resource<List<PaymentDetailEntity>>> = flow {
        emit(Resource.Loading())
        val resultData = localDataSource.getAllPaymentDetail()

        resultData
            .catch { e ->
                emit(Resource.Error(e.message.toString()))
            }
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

                    else -> Unit

                }

            }
    }

    override suspend fun getLatestBankDeposit(): Flow<Resource<BankDepositDetailEntity>> = flow {
        emit(Resource.Loading())
        val resultData = localDataSource.getBankDepositDetail()

        resultData
            .catch { e ->
                emit(Resource.Error(e.message.toString()))
            }
            .collect { result ->

                when (result) {
                    is Resource.Success -> {
                        if (result.data != null) {
                            emit(Resource.Success(result.data.mapToDomain()))
                        } else {
                            localDataSource.saveToBankDepositDb(
                                BankDepositDetailEntity(
                                    "BNI64",
                                    100_000_000
                                ).mapToData()
                            ).collect { upsert ->
                                when (upsert) {
                                    is Resource.Success -> {
                                        emit(
                                            Resource.Success(
                                                BankDepositDetailEntity(
                                                    "BNI64",
                                                    100_000_000
                                                )
                                            )
                                        )
                                    }

                                    is Resource.Error -> {
                                        emit(Resource.Error(upsert.message))
                                    }

                                    else -> Unit
                                }
                            }
                        }
                    }

                    is Resource.Error -> {

                        emit(Resource.Error(result.message))
                    }

                    else -> Unit

                }

            }
    }


}