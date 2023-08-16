package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.domain.repositories.PaymentRepository
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 16/08/2023
 */
class ClearPaymentDetailUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke() = paymentRepository.clearPaymentDetail()
}
