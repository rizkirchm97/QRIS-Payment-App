package com.rizki.qrispayment.common

import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}

fun String.splitQrCode(): List<String> = this.split(".")

fun Long.toCurrencyIDRFormat(): String {
    val formatter = DecimalFormat("#,###")
    formatter.decimalFormatSymbols = DecimalFormatSymbols.getInstance().apply {
        groupingSeparator = '.'
    }
    val number = this
    val format = formatter.format(number)
    return "Rp$format"
}
