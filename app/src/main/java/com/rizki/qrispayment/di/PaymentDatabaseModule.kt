package com.rizki.qrispayment.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rizki.qrispayment.data.datasource.local.PaymentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

@Module
@InstallIn(SingletonComponent::class)
object PaymentDatabaseModule {

    @Provides
    @Singleton
    fun providePaymentDatabase(@ApplicationContext context: Context): PaymentDatabase {
        return Room.databaseBuilder(
            context,
            PaymentDatabase::class.java,
            "PaymentDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePaymentDao(paymentDatabase: PaymentDatabase) = paymentDatabase.paymentDao

    @Provides
    @Singleton
    fun provideBankDepositDao(paymentDatabase: PaymentDatabase) = paymentDatabase.bankDepositDao
}