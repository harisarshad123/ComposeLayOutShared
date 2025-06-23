package com.example.composelayoutshared.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

@Composable
fun ShowScreenShotLayout(
    composableContent: @Composable () -> Unit,
    onComplete: () -> Unit
) {
    val context = LocalContext.current

    // factory - The block creating the View to be composed.
    AndroidView(
        factory = { ctx ->
            ComposeView(ctx).apply {
                // Set visibility to invisible to avoid flicker
                visibility = View.INVISIBLE
                setContent {
                    composableContent()
                }

                post {
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    draw(canvas)

                    val file = File(context.cacheDir, "images").apply { mkdirs() }
                    val imageFile = File(file, "screenShot.png")
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
                        putExtra(Intent.EXTRA_TEXT, "Share Receipt")
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

