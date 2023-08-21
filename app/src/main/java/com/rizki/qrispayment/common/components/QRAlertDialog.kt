package com.rizki.qrispayment.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizki.qrispayment.common.splitQrCode
import com.rizki.qrispayment.common.toCurrencyIDRFormat


@Composable
fun QRAlertDialog(
    title: String,
    message: String?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    val splitedMessage = message?.splitQrCode()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Send")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                if (message != null) {
                    LabelAndValue("TRANSACTION ID", splitedMessage?.get(1) ?: "No Data")
                    LabelAndValue("MERCHANT NAME", splitedMessage?.get(2) ?: "No Data")
                    LabelAndValue(
                        "AMOUNT",
                        splitedMessage?.get(3)?.toLong()?.toCurrencyIDRFormat() ?: "No Data"
                    )
                } else {
                    LabelAndValue("Transaction Id", "No Data")
                    LabelAndValue("Merchant Name", "No Data")
                    LabelAndValue("Amount", "No Data")
                }


            }


        }
    )


}



