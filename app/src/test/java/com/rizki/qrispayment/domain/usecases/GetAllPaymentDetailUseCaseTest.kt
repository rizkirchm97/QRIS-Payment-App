package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
class GetAllPaymentDetailUseCaseTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    private lateinit var getAllPaymentDetail: GetAllPaymentDetailUseCase

    @BeforeEach
    fun beforeEach() {
        getAllPaymentDetail = GetAllPaymentDetailUseCase(paymentRepository)
    }

    @Test
    @DisplayName("Should Get Success From repository and no error")
    fun testGetAllPaymentDetailSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getAllPaymentDetail()
        ).thenReturn(flowOf(Resource.Success(paymentsExpected)))

        // Act
        val result = getAllPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Success)
        assert((result as Resource.Success).data == paymentsExpected)
        Mockito.verify(paymentRepository, Mockito.times(1)).getAllPaymentDetail()

    }

    @Test
    @DisplayName("Should Get Error From repository")
    fun testGetAllPaymentDetailError() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getAllPaymentDetail()
        ).thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val result = getAllPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Error)
        assert((result as Resource.Error).message == "Error")
        Mockito.verify(paymentRepository, Mockito.times(1)).getAllPaymentDetail()

    }

    @Test
    @DisplayName("Should Get Empty From repository")
    fun testGetAllPaymentDetailEmpty() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getAllPaymentDetail()
        ).thenReturn(flowOf(Resource.Success(emptyList())))

        // Act
        val result = getAllPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Success)
        assert((result as Resource.Success).data == emptyList<PaymentDetailEntity>())
        Mockito.verify(paymentRepository, Mockito.times(1)).getAllPaymentDetail()

    }

    @Test
    @DisplayName("Should Get Loading From repository")
    fun testGetAllPaymentDetailLoading() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getAllPaymentDetail()
        ).thenReturn(flowOf(Resource.Loading()))

        // Act
        val result = getAllPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Loading)
        Mockito.verify(paymentRepository, Mockito.times(1)).getAllPaymentDetail()

    }

    private val paymentsExpected = listOf(
        PaymentDetailEntity(
            id = "BNI12364567",
            idTransaction = "ID3316547",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 250_000
        ),
        PaymentDetailEntity(
            id = "BNI12363145",
            idTransaction = "ID3351456",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 25_000
        ),
        PaymentDetailEntity(
            id = "BNI12366546",
            idTransaction = "ID3310879",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 15_900
        )
    )
}