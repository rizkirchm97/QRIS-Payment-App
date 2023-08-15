package com.rizki.qrispayment.di

import com.rizki.qrispayment.data.repositories.PaymentRepositoryImpl
import com.rizki.qrispayment.domain.repositories.PaymentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * created by RIZKI RACHMANUDIN on 15/08/2023
 */

@Module
@InstallIn(SingletonComponent::class)
interface PaymentRepositoryModule {

    @Binds
    @Singleton
    fun bindPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

}