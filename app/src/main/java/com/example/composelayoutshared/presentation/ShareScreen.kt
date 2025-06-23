package com.example.composelayoutshared.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composelayoutshared.utils.ShowScreenShotLayout

@Preview
@Composable
fun ShareScreen() {
    var showReceipt by remember { mutableStateOf(false) }
    var showUBLReceipt by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Compose Layout Shared As ScreenShot Example")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            showUBLReceipt = true
        }) {
            Text("Share UBL Receipt")
        }

        Button(onClick = {
            showReceipt = true
        }) {
            Text("ShowReceipt")
        }
    }

    if (showUBLReceipt) {
        ShowScreenShotLayout(
            composableContent = { TransactionDetailsScreen() },
            onComplete = {
                showUBLReceipt = false
            }
        )
    }

    if (showReceipt) {
        ShowScreenShotLayout(
            composableContent = { ReceiptLayout() },
            onComplete = {
                showReceipt = false
            }
        )
    }
}
