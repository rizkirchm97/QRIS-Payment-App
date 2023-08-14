package com.rizki.qrispayment.data.datasource.cache

import com.rizki.qrispayment.common.utils.DataStoreManager
import com.rizki.qrispayment.data.datasource.cache.model.PaymentCacheModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
class DataStoreCachedImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : DataStoreCached {
    override suspend fun saveToDataStore(paymentCacheModel: PaymentCacheModel) =
        dataStoreManager.saveToDataStore(paymentCacheModel)


    override fun getFromDataStore(): Flow<PaymentCacheModel> = dataStoreManager.getFromDataStore()


    override suspend fun clearDataStore() = dataStoreManager.clearDataStore()


}