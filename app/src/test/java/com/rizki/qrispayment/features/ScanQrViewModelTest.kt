package com.rizki.qrispayment.features

import androidx.lifecycle.SavedStateHandle
import com.rizki.qrispayment.common.getCombineBNIUUID
import com.rizki.qrispayment.common.splitQrCode
import com.rizki.qrispayment.common.testing.MainDispatcherRule
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import com.rizki.qrispayment.domain.usecases.SavePaymentUseCase
import com.rizki.qrispayment.features.scan_qr.ScanQREvent
import com.rizki.qrispayment.features.scan_qr.ScanQrState
import com.rizki.qrispayment.features.scan_qr.ScanQrViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@Extensions(
    value = [
        ExtendWith(MockitoExtension::class),
        ExtendWith(MainDispatcherRule::class)
    ]
)
class ScanQrViewModelTest {

    @Mock
    private lateinit var repo: PaymentRepository

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var paymentUseCase: SavePaymentUseCase

    private lateinit var viewModel: ScanQrViewModel

    @BeforeEach
    fun beforeEach() {
        savedStateHandle = mock(SavedStateHandle::class.java)
        paymentUseCase = SavePaymentUseCase(repo)
        viewModel = ScanQrViewModel(paymentUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Should return success when save payment_detail")
    fun testShouldReturnSuccess() = runTest {
        // Arrange
        Mockito.`when`(repo.savePayment(paymentStringExpected))
            .thenReturn(flowOf(Resource.Success("BNI12345600")))

        // Act
        viewModel.onEvent(ScanQREvent.SavePayment(paymentStringExpected))
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                if (it is ScanQrState.Success) {
                    assertInstanceOf(ScanQrState.Success::class.java, it)
                    assertEquals("BNI12345600", (it).data)
                }

            }
        }
        advanceTimeBy(50_000)
        Mockito.verify(repo).savePayment(paymentStringExpected)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Should return error when save payment_detail")
    fun testShouldReturnError() = runTest {

        // Arrange
        Mockito.`when`(repo.savePayment(paymentStringExpected))
            .thenReturn(flowOf(Resource.Error("Error")))

        // Act
        viewModel.onEvent(ScanQREvent.SavePayment(paymentStringExpected))
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                if (it is ScanQrState.Error) {
                    assertInstanceOf(ScanQrState.Error::class.java, it)
                    assertEquals("Error", (it).message)
                }
            }
        }

        advanceTimeBy(50_000)

        Mockito.verify(repo).savePayment(paymentStringExpected)

        job.cancel()

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @DisplayName("Should return loading when save payment_detail")
    fun testShouldReturnLoading() = runTest {

        // Arrange
        Mockito.`when`(repo.savePayment(paymentStringExpected))
            .thenReturn(flowOf(Resource.Loading()))

        // Act
        viewModel.onEvent(ScanQREvent.SavePayment(paymentStringExpected))
        val act = viewModel.uiState

        // Assert
        val job = launch {
            act.collect {
                if (it is ScanQrState.Loading) {
                    assertInstanceOf(ScanQrState.Loading::class.java, it)
                }
            }
        }

        advanceTimeBy(50_000)

        Mockito.verify(repo).savePayment(paymentStringExpected)

        job.cancel()

    }


    private val paymentExpected = PaymentDetailEntity(
        id = "BNI12345600",
        idTransaction = "ID3312445",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 250_000
    )

    private val paymentStringExpected = "BNI.ID12345678.MERCHANT MOCK TEST.50000"

}
