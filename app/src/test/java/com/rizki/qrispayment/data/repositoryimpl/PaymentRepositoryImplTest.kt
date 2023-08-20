package com.rizki.qrispayment.data.repositoryimpl

import com.rizki.qrispayment.common.getCombineBNIUUID
import com.rizki.qrispayment.common.splitQrCode
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.data.datasource.local.LocalDataSource
import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import com.rizki.qrispayment.data.mapper.mapToDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Should Get Success From localDataSource after save to DB")
    fun testSaveToPaymentDBSuccess() = runTest {

        val splitedStrinData = paymentStringExpected.splitQrCode()
        val id = getCombineBNIUUID()
        val paymentLocal = PaymentLocalEntity(
            id = id,
            bankId = "BNI64",
            bankSource = splitedStrinData[0],
            idTransaction = splitedStrinData[1],
            merchantName = splitedStrinData[2],
            totalAmount = splitedStrinData[3].toLong(),
        )

        Mockito.lenient().`when`(localDataSource.getBankDepositDetail()).thenReturn(flowOf(Resource.Success(bankLocalExpectedInput)))
        Mockito.lenient().`when`(localDataSource.saveToBankDepositDb(bankLocalExpectedOutput))
            .thenReturn(flowOf(Resource.Success(Unit)))
        Mockito.lenient().`when`(localDataSource.saveToPaymentDb(paymentLocal))
            .thenReturn(flowOf(Resource.Success("BNI12345600")))


        val job = launch {
            fakePaymentRepositoryImpl.savePayment(
                paymentStringExpected
            ).collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(Resource.Success("BNI12345600").data, result.data)
                }
            }
        }

        advanceTimeBy(50_000L)

//        Mockito.verify(localDataSource).getBankDepositDetail()

        job.cancel()

    }

    @Test
    @DisplayName("Should Get Error From localDataSource after save to DB")
    fun testSaveToPaymentDBError() = runTest {

        val expected = Exception("Error when save to db")

        // Arrange
        Mockito.lenient().`when`(localDataSource.getBankDepositDetail()).thenReturn(flowOf(Resource.Success(bankLocalExpectedInput)))
        Mockito.lenient().`when`(localDataSource.saveToBankDepositDb(bankLocalExpectedInput))
            .thenReturn(flowOf(Resource.Error(expected.message.toString())))
        Mockito.lenient().`when`(localDataSource.saveToPaymentDb(paymentLocalExpectedInput))
            .thenReturn(flowOf(Resource.Error(expected.message.toString())))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.savePayment(
                paymentStringExpected
            ).collect { result ->

                if (result is Resource.Error) {
                    assertInstanceOf(Resource.Error::class.java, result)
                    assertEquals(expected.message.toString(), result.message)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource).getBankDepositDetail()
        Mockito.verify(localDataSource).saveToBankDepositDb(bankLocalExpectedOutput)
        Mockito.verify(localDataSource).saveToPaymentDb(paymentLocalExpectedInput)

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Success From localDataSource when get payment detail by id")
    fun testGetPaymentDetailByIdSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(localDataSource.getPaymentDetailById(paymentLocalExpectedInput.id))
            .thenReturn(flowOf(Resource.Success(paymentLocalExpectedInput)))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getPaymentDetailById(paymentLocalExpectedInput.id).collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(paymentLocalExpectedInput.mapToDomain(), result.data)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getPaymentDetailById(paymentLocalExpectedInput.id)

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Error From localDataSource when get payment detail by id")
    fun testGetPaymentDetailByIdError() = runTest {

        val expected = Exception("Error when get payment detail by id")

        // Arrange
        Mockito.lenient().`when`(localDataSource.getPaymentDetailById(paymentLocalExpectedInput.id))
            .thenReturn(flowOf(Resource.Error(expected.message.toString())))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getPaymentDetailById(paymentLocalExpectedInput.id).collect { result ->

                if (result is Resource.Error) {
                    assertInstanceOf(Resource.Error::class.java, result)
                    assertEquals(expected.message.toString(), result.message)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getPaymentDetailById(paymentLocalExpectedInput.id)

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Success when clear all data payment")
    fun testClearAllDataSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(localDataSource.clearPaymentDetail()).thenReturn(flowOf(Resource.Success(Unit)))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.clearPaymentDetail().collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(Resource.Success(Unit).data, result.data)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).clearPaymentDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Error when clear all data payment")
    fun testClearAllDataError() = runTest {

        val expected = Exception("Error when clear all data payment")

        // Arrange
        Mockito.lenient().`when`(localDataSource.clearPaymentDetail()).thenReturn(flowOf(Resource.Error(expected.message.toString())))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.clearPaymentDetail().collect { result ->

                if (result is Resource.Error) {
                    assertInstanceOf(Resource.Error::class.java, result)
                    assertEquals(expected.message.toString(), result.message)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).clearPaymentDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Success when getAllPayment")
    fun testGetAllPaymentSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(localDataSource.getAllPaymentDetail()).thenReturn(flowOf(Resource.Success(listOf(paymentLocalExpectedInput))))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getAllPaymentDetail().collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(listOf(paymentLocalExpectedInput.mapToDomain()), result.data)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getAllPaymentDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Error when getAllPayment")
    fun testGetAllPaymentError() = runTest {

        val expected = Exception("Error when get all payment")

        // Arrange
        Mockito.lenient().`when`(localDataSource.getAllPaymentDetail()).thenReturn(flowOf(Resource.Error(expected.message.toString())))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getAllPaymentDetail().collect { result ->

                if (result is Resource.Error) {
                    assertInstanceOf(Resource.Error::class.java, result)
                    assertEquals(expected.message.toString(), result.message)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getAllPaymentDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Success when getLatestBankDeposit")
    fun testGetLatestBankDepositSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(localDataSource.getBankDepositDetail()).thenReturn(flowOf(Resource.Success(bankLocalExpectedInput)))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getLatestBankDeposit().collect { result ->

                if (result is Resource.Success) {
                    assertInstanceOf(Resource.Success::class.java, result)
                    assertEquals(bankLocalExpectedInput.mapToDomain(), result.data)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getBankDepositDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should Get Error when getLatestBankDeposit")
    fun testGetLatestBankDepositError() = runTest {

        val expected = Exception("Error when get latest bank deposit")

        // Arrange
        Mockito.lenient().`when`(localDataSource.getBankDepositDetail()).thenReturn(flowOf(Resource.Error(expected.message.toString())))

        // Act
        val job = launch {
            fakePaymentRepositoryImpl.getLatestBankDeposit().collect { result ->

                if (result is Resource.Error) {
                    assertInstanceOf(Resource.Error::class.java, result)
                    assertEquals(expected.message.toString(), result.message)
                }
            }
        }

        // Assert
        delay(1000L)

        Mockito.verify(localDataSource, Mockito.times(1)).getBankDepositDetail()

        job.cancel()
    }



    private val paymentLocalExpectedInput = PaymentLocalEntity(
        id = "BNI12366546",
        idTransaction = "ID3310879",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 15_900
    )

    private val bankLocalExpectedInput = BankDepositLocalEntity(
        bankId = "BNI64",
        nominalMoney = 1_000_000
    )

    private val bankLocalExpectedOutput = BankDepositLocalEntity(
        bankId = "BNI64",
        nominalMoney = 500_000_000
    )

    private val paymentStringExpected = "BNI.ID12345678.MERCHANT MOCK TEST.50000"

}