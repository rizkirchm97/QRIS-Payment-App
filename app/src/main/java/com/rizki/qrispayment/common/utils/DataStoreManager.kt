package com.rizki.qrispayment.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizki.qrispayment.data.datasource.cache.model.PaymentCacheModel
import kotlinx.coroutines.flow.map

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */

const val PREFS_NAME = "qris_payment"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)

class DataStoreManager(private val context: Context) {

    companion object {
        val IS_LOGIN = booleanPreferencesKey("is_login")
        val INITIAL_MONEY = intPreferencesKey("initial_money")
    }

    suspend fun saveToDataStore(paymentDetail: PaymentCacheModel) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGIN] = paymentDetail.isLogin
            preferences[INITIAL_MONEY] = paymentDetail.initialMoney
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        PaymentCacheModel(
            isLogin = it[IS_LOGIN] ?: false,
            initialMoney = it[INITIAL_MONEY] ?: 0
        )

    }

    suspend fun clearDataStore() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
