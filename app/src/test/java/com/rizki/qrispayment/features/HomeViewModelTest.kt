package com.rizki.qrispayment.features

import com.rizki.qrispayment.common.testing.MainDispatcherRule
import com.rizki.qrispayment.common.utils.Resource

import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity

import com.rizki.qrispayment.domain.repositories.PaymentRepository
import com.rizki.qrispayment.domain.usecases.GetLatestBankDepositUseCase
import com.rizki.qrispayment.features.home.HomeUiState
import com.rizki.qrispayment.features.home.HomeViewModel
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
import org.mockito.junit.jupiter.MockitoExtension


@Extensions(
    value = [ExtendWith(MockitoExtension::class), ExtendWith(MainDispatcherRule::class)]
)
class HomeViewModelTest {

    @Mock
    private lateinit var repo: PaymentRepository

    private lateinit var useCase: GetLatestBankDepositUseCase

    private lateinit var viewModel: HomeViewModel

    @BeforeEach
    fun beforeEach() {
        useCase = GetLatestBankDepositUseCase(repo)
        viewModel = HomeViewModel(useCase)
    }

    @Test
    @DisplayName("Should return success and not equal data when get latest bank deposit")
    fun testShouldReturnSuccessWhenGetLatestBankDeposit() = runTest {

        // Arrange
        Mockito.`when`(repo.getLatestBankDeposit())
            .thenReturn(flowOf(Resource.Success(bankExpectedInput)))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect {
                assert(it is HomeUiState.Success)
                assertNotEquals(bankExpectedOutput, (it as HomeUiState.Success).data)
            }
        }

        delay(1000L)

        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()


    }

    @Test
    @DisplayName("Should return error when get latest bank deposit")
    fun testShouldReturnErrorWhenGetLatestBankDeposit() = runTest {

        // Arrange
        Mockito.`when`(repo.getLatestBankDeposit())
            .thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect {
                assert(it is HomeUiState.Error)
                assertEquals("Error", (it as HomeUiState.Error).error)
            }
        }

        delay(1000L)

        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()



    }

    @Test
    @DisplayName("Should return loading when get latest bank deposit")
    fun testShouldReturnLoadingWhenGetLatestBankDeposit() = runTest {

        // Arrange
        Mockito.`when`(repo.getLatestBankDeposit())
            .thenReturn(flowOf(Resource.Loading()))

        // Act
        val actual = viewModel.uiState

        // Assert
        val job = launch {
            actual.collect {
                assert(it is HomeUiState.Loading)
                assertEquals(false, (it as HomeUiState.Loading).isLoading)
            }
        }

        delay(1000L)

        Mockito.verify(repo).getLatestBankDeposit()

        job.cancel()
    }

    private val bankExpectedInput = BankDepositDetailEntity(
        bankId = "BNI64", nominalMoney = 1_000_000
    )

    private val bankExpectedOutput = BankDepositDetailEntity(
        bankId = "BNI64", nominalMoney = 984_100
    )
}
