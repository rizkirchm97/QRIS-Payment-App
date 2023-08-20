package com.rizki.qrispayment.common.utils

object Utils {
    fun smartPad(data: List<String>): List<String> {
        val stringLength = data.maxOfOrNull { it.length } ?: 0
        val result = mutableListOf<String>()
        data.forEachIndexed { index, s ->

            result.add(s.padEnd(stringLength, ' '))

        }
        return result
    }
}