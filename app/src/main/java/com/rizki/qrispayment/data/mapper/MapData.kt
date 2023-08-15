package com.rizki.qrispayment.data.mapper

import com.rizki.qrispayment.data.datasource.local.entities.BankDepositLocalEntity
import com.rizki.qrispayment.data.datasource.local.entities.PaymentLocalEntity
import com.rizki.qrispayment.domain.entities.BankDepositDetailEntity
import com.rizki.qrispayment.domain.entities.PaymentDetailEntity

fun PaymentDetailEntity.mapToData() = PaymentLocalEntity(
    id = id,
    idTransaction = idTransaction,
    bankId = bankId,
    merchantName = merchantName,
    bankSource = bankSource,
    totalAmount = totalAmount
)

fun PaymentLocalEntity.mapToDomain() = PaymentDetailEntity(
    id = id,
    idTransaction = idTransaction ?: "",
    bankId = bankId ?: "",
    merchantName = merchantName ?: "",
    bankSource = bankSource ?: "",
    totalAmount = totalAmount ?: 0
)

fun BankDepositDetailEntity.mapToData() = BankDepositLocalEntity(
    bankId = bankId,
    nominalMoney = nominalMoney
)

fun BankDepositLocalEntity.mapToDomain() = BankDepositDetailEntity(
    bankId = bankId ?: "",
    nominalMoney = nominalMoney ?: 0
)