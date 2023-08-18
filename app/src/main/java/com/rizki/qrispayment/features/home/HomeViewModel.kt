package com.rizki.qrispayment.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizki.qrispayment.common.toCurrencyIDRFormat
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.usecases.GetLatestBankDepositUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getLatestBankDepositUseCase: GetLatestBankDepositUseCase
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = uiState(getLatestBankDepositUseCase)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading(isLoading = true)
        )


    private fun uiState(useCase: GetLatestBankDepositUseCase): Flow<HomeUiState> = flow {
        val result = useCase.invoke()

        result.collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    emit(HomeUiState.Loading(isLoading = false))
                }

                is Resource.Success -> {
                    emit(HomeUiState.Success(resource.data, resource.data?.nominalMoney?.toCurrencyIDRFormat() ?: "0"))
                }

                is Resource.Error -> {
                    emit(HomeUiState.Error(resource.message.toString()))
                }
            }
        }
    }

}

sealed interface HomeUiState {
    data class Loading(val isLoading: Boolean) : HomeUiState
    data class Success(val data: BankDepositDetailEntity?, val nominalStirng: String) : HomeUiState
    data class Error(val error: String) : HomeUiState
}