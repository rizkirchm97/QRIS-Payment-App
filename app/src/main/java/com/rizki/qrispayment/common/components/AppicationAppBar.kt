package com.rizki.qrispayment.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationAppBar(title: String) {
    TopAppBar(title = { Text(text = title, style = TextStyle(color = Color.White)) }, colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = Color(0xFFE55300)
    ), modifier = Modifier.background(Color.Transparent),
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationAppbar(
    title:String,
    onClickBack:()->Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = {
            Text(text = title, style = TextStyle(color = Color.White))
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Color.White,
                contentDescription = "",
                modifier = modifier.clickable {
                    onClickBack()
                }
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFFE55300)
        ),
    )
}