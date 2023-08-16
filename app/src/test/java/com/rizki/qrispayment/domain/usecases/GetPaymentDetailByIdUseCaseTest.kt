package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
class GetPaymentDetailByIdUseCaseTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    private lateinit var getPaymentDetailByIdUseCase: GetPaymentDetailByIdUseCase

    @BeforeEach
    fun beforeEach() {
        getPaymentDetailByIdUseCase = GetPaymentDetailByIdUseCase(paymentRepository)
    }

    @Test
    @DisplayName("Should Get Success From repository and no error")
    fun testGetPaymentDetailByIdSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getPaymentDetailById(
                idTransaction = "BNI12364567"
            )
        ).thenReturn(flowOf(Resource.Success(paymentsExpected)))

        // Act
        val result = getPaymentDetailByIdUseCase.invoke(
            idTransaction = "BNI12364567"
        ).first()

        // Assert
        assert(result is Resource.Success)
        assert((result as Resource.Success).data == paymentsExpected)

    }

    @Test
    @DisplayName("Should Get Error From repository")
    fun testGetPaymentDetailIdError() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getPaymentDetailById(
                idTransaction = "BNI12364567"
            )
        ).thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val result = getPaymentDetailByIdUseCase.invoke(
            idTransaction = "BNI12364567"
        ).first()

        // Assert
        assert(result is Resource.Error)
        assert((result as Resource.Error).message == "Error")
        Mockito.verify(paymentRepository).getPaymentDetailById(
            idTransaction = "BNI12364567")

    }

    @Test
    @DisplayName("Should Get Loading From repository")
    fun testGetPaymentDetailIdLoading() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getPaymentDetailById(
                idTransaction = "BNI12364567"
            )
        ).thenReturn(flowOf(Resource.Loading()))

        // Act
        val result = getPaymentDetailByIdUseCase.invoke(
            idTransaction = "BNI12364567"
        ).first()

        // Assert
        assert(result is Resource.Loading)
        Mockito.verify(paymentRepository).getPaymentDetailById(
            idTransaction = "BNI12364567")

    }

    @Test
    @DisplayName("Id parameter should not be null or empty")
    fun testGetPaymentDetailIdNull() = runTest {


        val expected = assertThrows<IllegalArgumentException>() {
            getPaymentDetailByIdUseCase.invoke(
                idTransaction = "  "
            )
            throw IllegalArgumentException("Id parameter should not be null or empty")
        }

        assertEquals("Id parameter should not be null or empty", expected.message)



    }

    private val paymentsExpected = PaymentDetailEntity(
        id = "BNI12364567",
        idTransaction = "ID3316547",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 250_000
    )

}