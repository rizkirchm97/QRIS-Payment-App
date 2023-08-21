package com.rizki.qrispayment.features

import androidx.lifecycle.SavedStateHandle
import com.rizki.qrispayment.common.testing.MainDispatcherRule
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import com.rizki.qrispayment.domain.usecases.GetAllPaymentDetailUseCase
import com.rizki.qrispayment.features.payment_histories.PaymentHistoriesUiState
import com.rizki.qrispayment.features.payment_histories.PaymentHistoriesViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
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
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */

@Extensions(
    value = [
        ExtendWith(MockitoExtension::class),
        ExtendWith(MainDispatcherRule::class)
    ]
)
class PaymentHistoriesViewModelTest {

    @Mock
    private lateinit var repo: PaymentRepository


    @Mock
    private lateinit var paymentUseCase: GetAllPaymentDetailUseCase

    private lateinit var viewModel: PaymentHistoriesViewModel

    @BeforeEach
    fun beforeEach() {
        paymentUseCase = GetAllPaymentDetailUseCase(repo)
        viewModel = PaymentHistoriesViewModel(paymentUseCase)
    }

    @Test
    @DisplayName("Should return success when get all payment_detail")
    fun testShouldReturnSuccess() = runTest {

        // Arrange
        Mockito.`when`(repo.getAllPaymentDetail()).thenReturn(flowOf(Resource.Success(paymentExpectedInput)))


        // Act
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                if (it is PaymentHistoriesUiState.Success) {
                    assertEquals(paymentExpectedInput, it.data)
                    assertEquals(paymentExpectedInput.size, it.data?.size)
                }
            }
        }

        delay(1000L)

        Mockito.verify(repo).getAllPaymentDetail()

        job.cancel()

    }

    @Test
    @DisplayName("Should return error when get all payment_detail")
    fun testShouldReturnError() = runTest {

        // Arrange
        Mockito.`when`(repo.getAllPaymentDetail()).thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                if (it is PaymentHistoriesUiState.Error) {
                    assertEquals("Error", it.message)
                }
            }
        }

        delay(1000L)

        Mockito.verify(repo).getAllPaymentDetail()

        job.cancel()
    }

    @Test
    @DisplayName("Should return loading when get all payment_detail")
    fun testShouldReturnLoading() = runTest {

        // Arrange
        Mockito.`when`(repo.getAllPaymentDetail()).thenReturn(flowOf(Resource.Loading()))

        // Act
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                assertInstanceOf(PaymentHistoriesUiState.Loading::class.java, it)
            }
        }

        delay(1000L)

        Mockito.verify(repo).getAllPaymentDetail()

        job.cancel()
    }

    private val paymentExpectedInput = listOf(
        PaymentDetailEntity(
            id = "BNI12343",
            idTransaction = "ID3310879",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 54_958
        ),
        PaymentDetailEntity(
            id = "BNI12987",
            idTransaction = "ID3310879",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 120_000
        ),
        PaymentDetailEntity(
            id = "BNI1239387",
            idTransaction = "ID3310879",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 89_980
        ),
        PaymentDetailEntity(
            id = "BNI1235436",
            idTransaction = "ID3310879",
            bankId = "BNI64",
            merchantName = "KOPKAR BNI",
            bankSource = "BNI",
            totalAmount = 432_999
        )
    )
}