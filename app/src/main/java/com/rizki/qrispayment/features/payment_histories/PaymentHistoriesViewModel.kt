package com.rizki.qrispayment.features.payment_histories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.usecases.GetAllPaymentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 21/08/2023
 */

@HiltViewModel
class PaymentHistoriesViewModel @Inject constructor(
    getAllPaymentDetailUseCase: GetAllPaymentDetailUseCase
): ViewModel() {

    val uiState: StateFlow<PaymentHistoriesUiState> = uiState(getAllPaymentDetailUseCase)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PaymentHistoriesUiState.Loading(isLoading = true)
        )

    private fun uiState(
        getAllPaymentDetailUseCase: GetAllPaymentDetailUseCase
    ): Flow<PaymentHistoriesUiState> = flow {
        getAllPaymentDetailUseCase.invoke().collect {result ->
            when(result) {
                is Resource.Success -> {
                    emit(PaymentHistoriesUiState.Success(result.data))
                }
                is Resource.Error -> {
                    emit(PaymentHistoriesUiState.Error(result.message))
                }
                is Resource.Loading -> {
                    emit(PaymentHistoriesUiState.Loading(result.isLoading))
                }
            }
        }
    }

}

sealed class PaymentHistoriesUiState {
    data class Success(val data: List<PaymentDetailEntity>?) : PaymentHistoriesUiState()
    data class Error(val message: String?) : PaymentHistoriesUiState()
    data class Loading(val isLoading: Boolean) : PaymentHistoriesUiState()
}

