package com.rizki.qrispayment.domain.usecases

import com.rizki.qrispayment.common.utils.Resource
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 16/08/2023
 */
class SavePaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        paymentDetail: String?
    ): Flow<Resource<String>> = paymentRepository.savePayment(
            paymentDetail = paymentDetail
        )


}