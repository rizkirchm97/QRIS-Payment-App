package com.rizki.qrispayment.data.datasource.cache

import com.rizki.qrispayment.data.datasource.cache.model.PaymentCacheModel
import kotlinx.coroutines.flow.Flow

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
interface DataStoreCached {
    suspend fun saveToDataStore(paymentCacheModel: PaymentCacheModel)
    fun getFromDataStore(): Flow<PaymentCacheModel>
    suspend fun clearDataStore()
}