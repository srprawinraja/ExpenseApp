package com.example.expenseapp.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

fun createImageUri(context: Context): Uri {
    val imagesDir = File(context.cacheDir, "images")
    if (!imagesDir.exists()) imagesDir.mkdirs()

    val file = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
    Log.d("img uri", context.packageName+" "+"com.example.expenseapp")
    val uri =  FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    val fileExists = File(uri!!.path ?: "").exists()
    Log.d("CameraCheck", "File exists: $fileExists, path=${uri!!.path}")
    return uri
}

