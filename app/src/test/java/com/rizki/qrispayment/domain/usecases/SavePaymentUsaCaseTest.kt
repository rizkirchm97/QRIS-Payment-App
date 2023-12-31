package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class SavePaymentUsaCaseTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    private lateinit var savePaymentUsaCase: SavePaymentUseCase

    @BeforeEach
    fun beforeEach() {
        savePaymentUsaCase = SavePaymentUseCase(paymentRepository)
    }

    @Test
    @DisplayName("Should Get Success From repository and no error while save payment_detail")
    fun testSavePaymentSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.savePayment(
                paymentStringExpected
            )
        ).thenReturn(flowOf(Resource.Success("BNI12345600")))

        // Act
        val result = savePaymentUsaCase.invoke(
            paymentStringExpected
        ).first()

        // Assert
        assert(result is Resource.Success)
        Mockito.verify(paymentRepository).savePayment(
            paymentStringExpected)


    }

    @Test
    @DisplayName("Should Get Error From repository and error while save payment_detail")
    fun testSavePaymentError() = runTest {

        val expected = "Error while save payment_detail"

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.savePayment(
                paymentStringExpected
            )
        ).thenReturn(flowOf(Resource.Error(expected)))

        // Act
        val result = savePaymentUsaCase.invoke(
            paymentStringExpected
        ).first()

        // Assert
        assertEquals(expected, result.message)
        assert(result is Resource.Error)
        Mockito.verify(paymentRepository).savePayment(
            paymentStringExpected)

    }

    @Test
    @DisplayName("Should Get Loading From repository")
    fun testSavePaymentLoading() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.savePayment(
                paymentStringExpected
            )
        ).thenReturn(flowOf(Resource.Loading()))

        // Act
        val result = savePaymentUsaCase.invoke(
            paymentStringExpected
        ).first()

        // Assert
        assert(result is Resource.Loading)
        Mockito.verify(paymentRepository).savePayment(
            paymentStringExpected)

    }

    @Test
    @DisplayName("Id parameter should not be null or empty")
    fun testGetPaymentDetailIdNull() = runTest {


        val expected = org.junit.jupiter.api.assertThrows<IllegalArgumentException>() {
            savePaymentUsaCase.invoke(
                paymentStringExpected
            )
            throw IllegalArgumentException("All contained parameter should not be null or empty")
        }

        assertEquals("All contained parameter should not be null or empty", expected.message)



    }


    private val paymentExpected = PaymentDetailEntity(
        id = "BNI12345600",
        idTransaction = "ID3312445",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 250_000
    )

    private val bankExpected = BankDepositDetailEntity(
        bankId = "BNI64",
        nominalMoney = 1_000_000
    )

    private val paymentStringExpected = "BNI.ID12345678.MERCHANT MOCK TEST.50000"


}