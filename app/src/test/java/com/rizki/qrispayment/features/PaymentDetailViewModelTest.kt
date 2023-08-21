package com.rizki.qrispayment.features

import androidx.lifecycle.SavedStateHandle
import com.rizki.qrispayment.common.testing.MainDispatcherRule
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import com.rizki.qrispayment.domain.usecases.GetLatestBankDepositUseCase
import com.rizki.qrispayment.domain.usecases.GetPaymentDetailByIdUseCase
import com.rizki.qrispayment.features.payment_detail.PaymentDataHolder
import com.rizki.qrispayment.features.payment_detail.PaymentUiState
import com.rizki.qrispayment.features.payment_detail.PaymentDetailViewModel
import kotlinx.coroutines.delay
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

@Extensions(
    value = [ExtendWith(MockitoExtension::class), ExtendWith(MainDispatcherRule::class)]
)
class PaymentDetailViewModelTest {

    @Mock
    private lateinit var repo: PaymentRepository

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var paymentUseCase: GetPaymentDetailByIdUseCase
    private lateinit var bankUseCase: GetLatestBankDepositUseCase


    private lateinit var viewModel: PaymentDetailViewModel

    @BeforeEach
    fun beforeEach() {
        savedStateHandle = mock(SavedStateHandle::class.java)
        paymentUseCase = GetPaymentDetailByIdUseCase(repo)
        bankUseCase = GetLatestBankDepositUseCase(repo)
        viewModel = PaymentDetailViewModel(paymentUseCase, bankUseCase, savedStateHandle)
    }

    @Test
    @DisplayName("Should return success when get latest bank deposit")
    fun testShouldReturnSuccess() = runTest {


        // Arrange
        Mockito.`when`(savedStateHandle.get<String>("idTransaction"))
            .thenReturn("ID3310879")
        Mockito.`when`(savedStateHandle.get<String>("idTransaction")
            ?.let { repo.getPaymentDetailById(it) })
            .thenReturn(flowOf(Resource.Success(paymentExpectedInput)))
        Mockito.`when`(repo.getLatestBankDeposit())
            .thenReturn(flowOf(Resource.Success(bankExpectedInput)))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect { result ->
                if (result is PaymentUiState.Success) {
                    assertEquals(
                        PaymentDataHolder(paymentExpectedInput, bankExpectedOutput),
                        (result as PaymentUiState.Success).data
                    )
                    assertEquals(
                        result.data?.paymentDetailEntity?.idTransaction,
                        "ID3310879"
                    )
                    assertTrue(result.data?.paymentDetailEntity?.id?.isNotEmpty()!!)


                }
            }


        }

        delay(50_000L)

        Mockito.verify(repo).getPaymentDetailById(
            savedStateHandle.get<String>("idTransaction")!!
        )
        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()
    }

    @Test
    @DisplayName("Should return error when get latest bank deposit")
    fun testShouldReturnError() = runTest {

        // Arrange
        Mockito.`when`(savedStateHandle.get<String>("idTransaction"))
            .thenReturn("ID3310879")
        Mockito.`when`(repo.getPaymentDetailById(savedStateHandle.get<String>("idTransaction")!!))
            .thenReturn(flowOf(Resource.Error("Error")))
        Mockito.`when`(repo.getLatestBankDeposit()).thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect { result ->
                if (result is PaymentUiState.Error) {
                    assertEquals("Error", result.error)
                }
            }
        }

        delay(50_000L)

        Mockito.verify(repo).getPaymentDetailById(
            savedStateHandle.get<String>("idTransaction")!!
        )
        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()
    }

    @Test
    @DisplayName("Should return loading when get latest bank deposit")
    fun testShouldReturnLoading() = runTest {

        // Arrange
        Mockito.`when`(savedStateHandle.get<String>("idTransaction"))
            .thenReturn("ID3310879")
        Mockito.`when`(repo.getPaymentDetailById(savedStateHandle.get<String>("idTransaction")!!))
            .thenReturn(flowOf(Resource.Loading()))
        Mockito.`when`(repo.getLatestBankDeposit()).thenReturn(flowOf(Resource.Loading()))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect { result ->
                if (result is PaymentUiState.Loading) {
                    assertEquals(true, result.isLoading)
                }
            }
        }

        delay(50_000L)

        Mockito.verify(repo).getPaymentDetailById(
            savedStateHandle.get<String>("idTransaction")!!
        )
        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()
    }

    private val bankExpectedInput = BankDepositDetailEntity(
        bankId = "BNI64", nominalMoney = 1_000_000
    )

    private val bankExpectedOutput = BankDepositDetailEntity(
        bankId = "BNI64", nominalMoney = 984_100
    )

    private val paymentExpectedInput = PaymentDetailEntity(
        id = "BNI12366546",
        idTransaction = "ID3310879",
        bankId = "BNI64",
        merchantName = "KOPKAR BNI",
        bankSource = "BNI",
        totalAmount = 15_900
    )
}


