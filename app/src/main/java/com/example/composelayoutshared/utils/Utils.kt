package com.example.composelayoutshared.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import com.example.composelayoutshared.presentation.TransactionDetailsScreen
import java.io.File
import java.io.FileOutputStream

@Composable
fun HiddenReceiptView( onComplete: () -> Unit) {
    val context = LocalContext.current

    // factory - The block creating the View to be composed.
    AndroidView(
        factory = { ctx ->
            ComposeView(ctx).apply {
                // Set visibility to invisible to avoid flicker
                visibility = View.INVISIBLE
                setContent {
//                    ReceiptLayout(data)
                    TransactionDetailsScreen()
                }

                post {
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    draw(canvas)

                    val file = File(context.cacheDir, "images").apply { mkdirs() }
                    val imageFile = File(file, "receipt_${System.currentTimeMillis()}.png")
                    FileOutputStream(imageFile).use {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    }

                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )

                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_TEXT, "UBL Receipt")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    context.startActivity(Intent.createChooser(intent, "Share Receipt"))
                    onComplete()
                }
            }
        },
        modifier = Modifier
            .wrapContentSize()
            .alpha(0f)
    )
}

@Composable
fun ReceiptLayout(data: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE3F2FD),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("UBL Digital Receipt", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            HorizontalDivider()
            Text(data, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Thank you for using UBL!", fontStyle = FontStyle.Italic)
        }
    }
}
