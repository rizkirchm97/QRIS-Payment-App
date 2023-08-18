package com.rizki.qrispayment.features

import androidx.lifecycle.SavedStateHandle
import com.rizki.qrispayment.common.testing.MainDispatcherRule
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import com.rizki.qrispayment.domain.usecases.GetPaymentDetailByIdUseCase
import com.rizki.qrispayment.domain.usecases.SavePaymentUseCase
import com.rizki.qrispayment.features.scan_qr.ScanQrViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extensions
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension

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

        paymentUseCase = SavePaymentUseCase(repo)
        viewModel = ScanQrViewModel(paymentUseCase)
    }

    @Test
    fun testShouldReturnSuccess() = runTest {
        // Arrange
        Mockito.`when`(repo.savePayment(paymentExpected))
            .thenReturn(flowOf(Resource.Success(Unit)))

        // Act
        val act = viewModel.savePayment(paymentStringExpected)

        // Assert
        assertEquals(Resource.Success(Unit), act)
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

    private val bankExpected = BankDepositDetailEntity(
        bankId = "BNI64",
        nominalMoney = 1_000_000
    )

}
