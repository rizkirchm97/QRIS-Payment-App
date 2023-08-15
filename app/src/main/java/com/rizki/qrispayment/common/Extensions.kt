package com.rizki.qrispayment.common

import java.nio.ByteBuffer

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}

fun String.splitQrCode(): List<String> = this.split(".")
