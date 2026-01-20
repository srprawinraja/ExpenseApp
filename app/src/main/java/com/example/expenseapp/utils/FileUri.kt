package com.example.expenseapp.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object FileUri {
    fun createImageUri(context: Context): Uri {
        val imagesDir = File(context.cacheDir, "images")
        if (!imagesDir.exists()) imagesDir.mkdirs()

        val file = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        return uri
    }

    fun uriToFile(context: Context, filesStored: List<com.example.expenseapp.data.ui.File>): List<File> {
        val files = mutableListOf<File>()
        for (file in filesStored) {
            val uri = file.uri
            if(uri!=null) {
                val inputStream = context.contentResolver.openInputStream(uri) ?: continue
                val file = File.createTempFile("upload_", ".png", context.cacheDir)
                inputStream.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                files.add(file)
            }
        }
        return files
    }


    fun fileToMultipart(files: List<File>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        for (file in files) {
            val requestBody =
                file.asRequestBody("image/*".toMediaTypeOrNull())

            parts.add(
                MultipartBody.Part.createFormData(
                    name = "files",
                    filename = file.name,
                    body = requestBody
                )
            )
        }
        return parts
    }
}
