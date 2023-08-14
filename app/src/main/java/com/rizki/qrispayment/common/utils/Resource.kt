package com.rizki.qrispayment.common.utils

/**
 * created by RIZKI RACHMANUDIN on 14/08/2023
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String?) : Resource<T>(null, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null, null)
}
