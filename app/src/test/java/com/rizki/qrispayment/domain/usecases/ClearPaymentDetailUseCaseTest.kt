package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
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
class ClearPaymentDetailUseCaseTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    private lateinit var clearPaymentDetail: ClearPaymentDetailUseCase

    @BeforeEach
    fun beforeEach() {
        clearPaymentDetail = ClearPaymentDetailUseCase(paymentRepository)
    }

    @Test
    @DisplayName("Should Get Success From repository after delete all data")
    fun testDeletePromoSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(paymentRepository.clearPaymentDetail())
            .thenReturn(flowOf(Resource.Success(Unit)))

        // Act
        val result = clearPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Success)
        assert((result as Resource.Success).data == Unit)

        Mockito.verify(paymentRepository).clearPaymentDetail()

    }

    @Test
    @DisplayName("Should Get Error From repository after delete all data")
    fun testDeletePromoSError() = runTest {

        // Arrange
        Mockito.lenient().`when`(paymentRepository.clearPaymentDetail())
            .thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val result = clearPaymentDetail.invoke().first()

        // Assert
        assert(result is Resource.Error)
        assert(("Error") == result.message)

        Mockito.verify(paymentRepository).clearPaymentDetail()

    }


}