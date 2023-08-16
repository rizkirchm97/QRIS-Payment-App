package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
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
class GetLatestBankDepositUseCaseTest {

    @Mock
    lateinit var paymentRepository: PaymentRepository

    private lateinit var getLatestBankDepositUseCase: GetLatestBankDepositUseCase

    @BeforeEach
    fun beforeEach() {
        getLatestBankDepositUseCase = GetLatestBankDepositUseCase(paymentRepository)
    }

    @Test
    @DisplayName("Should Get Success From repository and no error")
    fun testGetLatestBankDepositSuccess() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getLatestBankDeposit()
        ).thenReturn(flowOf(Resource.Success(bankExpected)))

        // Act
        val result = getLatestBankDepositUseCase.invoke().first()

        // Assert
        assert(result is Resource.Success)
        assertEquals(Resource.Success(bankExpected).data, result.data)
        Mockito.verify(paymentRepository, Mockito.times(1)).getLatestBankDeposit()

    }

    @Test
    @DisplayName("Should Get Error From repository and no data")
    fun testGetLatestBankDepositError() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getLatestBankDeposit()
        ).thenReturn(flowOf(Resource.Error("Error")))

        // Act
        val result = getLatestBankDepositUseCase.invoke().first()

        // Assert
        assert(result is Resource.Error)
        assertEquals("Error", result.message)
        Mockito.verify(paymentRepository, Mockito.times(1)).getLatestBankDeposit()

    }

    @Test
    @DisplayName("Should Get Loading From repository")
    fun testGetLatestBankDepositLoading() = runTest {

        // Arrange
        Mockito.lenient().`when`(
            paymentRepository.getLatestBankDeposit()
        ).thenReturn(flowOf(Resource.Loading()))

        // Act
        val result = getLatestBankDepositUseCase.invoke().first()

        // Assert
        assert(result is Resource.Loading)
        Mockito.verify(paymentRepository, Mockito.times(1)).getLatestBankDeposit()

    }


    private val bankExpected = BankDepositDetailEntity(
        bankId = "BNI64",
        nominalMoney = 1_000_000
    )


}