package com.rizki.qrispayment.features.scan_qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizki.qrispayment.common.splitQrCode
import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.usecases.SavePaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ScanQrViewModel @Inject constructor(
    private val useCase: SavePaymentUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow<ScanQrViewModelState>(ScanQrViewModelState(isLoading = true))
    val uiState = _viewModelState
        .map(ScanQrViewModelState::toUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = _viewModelState.value.toUiState()
        )

    fun onEvent(event: ScanQREvent) {
        when (event) {
            is ScanQREvent.SavePayment -> {
                savePayment(event.paymentDetail)
            }
        }
    }

    private fun savePayment(paymentDetail: String): Flow<ScanQrState> = flow {
        viewModelScope.launch {
            val qrCOdeDetail = paymentDetail.splitQrCode()
            val paymentDetailEntity = PaymentDetailEntity(
                id = UUID.randomUUID().toString(),
                bankSource = qrCOdeDetail[0],
                idTransaction = qrCOdeDetail[1],
                merchantName = qrCOdeDetail[2],
                totalAmount = qrCOdeDetail[3].toLong(),
                bankId = "BNI64"
            )
            useCase.invoke(paymentDetailEntity).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        emit(ScanQrState.Success(result.data))
                    }

                    is Resource.Error -> {
                        emit(ScanQrState.Error(result.message))
                    }

                    is Resource.Loading -> {
                        emit(ScanQrState.Loading(result.isLoading))
                    }
                }
            }
        }
    }


}

private data class ScanQrViewModelState(
    val isLoading: Boolean = false,
    val success: Unit? = Unit,
    val isError: Boolean? = false,
    val message: String? = null
) {
    fun toUiState(): ScanQrState {
        return when {
            isLoading -> ScanQrState.Loading(isLoading)
            success != null -> ScanQrState.Success(success)
            isError == true -> ScanQrState.Error(message)
            else -> throw IllegalStateException("Invalid state")
        }
    }
}

sealed interface ScanQREvent{
    data class SavePayment(val paymentDetail: String) : ScanQREvent
}

sealed interface ScanQrState {
    data class Loading(val isLoading: Boolean?) : ScanQrState
    data class Success(val data: Unit?) : ScanQrState
    data class Error(val message: String?) : ScanQrState
}