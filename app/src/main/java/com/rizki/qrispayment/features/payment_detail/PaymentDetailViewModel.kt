package com.rizki.qrispayment.features.payment_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.usecases.GetLatestBankDepositUseCase
import com.rizki.qrispayment.domain.usecases.GetPaymentDetailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PaymentDetailViewModel @Inject constructor(
    getPaymentDetailByIdUseCase: GetPaymentDetailByIdUseCase,
    getLatestBankDepositUseCase: GetLatestBankDepositUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {



    val uiState: StateFlow<PaymentUiState> = uIState(
        getPaymentDetailByIdUseCase,
        getLatestBankDepositUseCase,
        savedStateHandle
    ).stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PaymentUiState.Loading(isLoading = true)
    )

    private fun uIState(
        getPaymentDetailByIdUseCase: GetPaymentDetailByIdUseCase,
        getLatestBankDepositUseCase: GetLatestBankDepositUseCase,
        savedStateHandle: SavedStateHandle
    ): Flow<PaymentUiState> = flow {

        val idTransaction: String? = savedStateHandle.get<String>("paymentId")

        if (idTransaction.isNullOrBlank()) {
            emit(PaymentUiState.Loading(isLoading = false))
            emit(PaymentUiState.Error("Id Transaction is null or blank"))
            return@flow
        }

        getPaymentDetailByIdUseCase.invoke(idTransaction)
            .combine(getLatestBankDepositUseCase.invoke()) { paymentDetail, bankDepositDetail ->
                when {
                    paymentDetail is Resource.Success && bankDepositDetail is Resource.Success -> {
                        emit(
                            PaymentUiState.Success(
                                PaymentDataHolder(
                                    paymentDetail.data!!,
                                    bankDepositDetail.data!!
                                )
                            )
                        )
                    }

                    paymentDetail is Resource.Loading && bankDepositDetail is Resource.Loading -> {
                        emit(PaymentUiState.Loading(isLoading = false))
                    }

                    paymentDetail is Resource.Error && bankDepositDetail is Resource.Error -> {
                        emit(PaymentUiState.Error("Error"))
                    }
                }


            }.collect()

    }


}

sealed interface PaymentUiState {
    data class Loading(val isLoading: Boolean) : PaymentUiState
    data class Success(val data: PaymentDataHolder?) : PaymentUiState
    data class Error(val error: String) : PaymentUiState
}

data class PaymentDataHolder(
    val paymentDetailEntity: PaymentDetailEntity,
    val bankDepositDetailEntity: BankDepositDetailEntity
)

