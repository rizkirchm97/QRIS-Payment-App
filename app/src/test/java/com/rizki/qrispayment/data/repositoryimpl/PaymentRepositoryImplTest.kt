package com.rizki.qrispayment.data.repositoryimpl

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.LocalDataSource
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import com.rizki.qrispayment.data.mapper.mapToData
import com.rizki.qrispayment.data.mapper.mapToDomain
import com.rizki.qrispayment.data.repositories.PaymentRepositoryImpl
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

/**
 * created by RIZKI RACHMANUDIN on 16/08/2023
 */

@Extensions(
    value = [
        ExtendWith(MockitoExtension::class)
    ]
)
class PaymentRepositoryImplTest {

    @Mock
    lateinit var localDataSource: LocalDataSource

    private lateinit var fakePaymentRepositoryImpl: FakePaymentRepositoryImpl

    @BeforeEach
    fun beforeEach() {
        fakePaymentRepositoryImpl = FakePaymentRepositoryImpl(localDataSource)
    }

    @Test
    @DisplayName("Should Get Success From localDataSource after save to DB")
    fun testSaveToPaymentDBSuccess() = runTest {

        // Arrange
//        Mockito.lenient().`when`(localDataSource.getBankDepositDetail()).thenReturn(flowOf(Resource.Success(bankLocalExpected)))
//        Mockito.lenient().`when`(localDataSource.saveToPaymentDb(paymentLocalExpected))
//            .thenReturn(flowOf(Resource.Success(Unit)))
        Mockito.lenient().`when`(localDataSource.saveToBankDepositDb(bankLocalExpected))
            .thenReturn(flowOf(Resource.Success(Unit)))


        val job = launch {
            fakePaymentRepositoryImpl.savePayment(
                paymentLocalExpected.mapToDomain(),
                bankLocalExpected.mapToDomain()
            ).collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(Resource.Success(Unit).data, result.data)
                }
            }
        }



        delay(50_000L)

//        Mockito.verify(localDataSource, Mockito.times(1)).saveToPaymentDb(paymentLocalExpected)
        Mockito.verify(localDataSource, Mockito.times(1)).saveToBankDepositDb(bankLocalExpected)
//        Mockito.verify(localDataSource, Mockito.times(1)).getBankDepositDetail()

        job.cancel()

    }

    private val paymentLocalExpected = PaymentLocalEntity(
        id = "BNI12366546",
        idTransaction = "ID3310879",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 15_900
    )

    private val bankLocalExpected = BankDepositLocalEntity(
        bankId = "BNI64",
        nominalMoney = 1_000_000
    )

}