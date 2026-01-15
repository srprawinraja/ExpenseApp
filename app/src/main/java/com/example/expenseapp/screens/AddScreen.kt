package com.example.expenseapp.screens

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.expenseapp.components.CustomTextFieldComponent
import com.example.expenseapp.utils.createImageUri
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddScreen (){
    val TAG: String = "AddScreen"
    val context = LocalContext.current
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formatted = current.format(formatter)
    var date by remember { mutableStateOf(formatted) }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isIncomeButtonClicked by remember { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var pass by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            pass = true
            val inputStream = context.contentResolver.openInputStream(imageUri!!)
            if (inputStream != null) {
                Log.d("CameraCheck", "File exists via ContentResolver!")
                inputStream.close()
            } else {
                Log.d("CameraCheck", "Cannot read file yet")
            }
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            imageUri = uri
            launcher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    isIncomeButtonClicked = !isIncomeButtonClicked
                },
                colors = ButtonColors(
                    containerColor = if(isIncomeButtonClicked) Color.Black
                    else Color.White,
                    contentColor = Color.Cyan,
                    disabledContentColor = Color.Cyan,
                    disabledContainerColor = Color.White,
                )
            ) {
                Text("Income")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    isIncomeButtonClicked = !isIncomeButtonClicked
                },
                colors = ButtonColors(
                    containerColor = if(isIncomeButtonClicked) Color.White
                    else Color.Black,
                    contentColor = Color.Cyan,
                    disabledContentColor = Color.Cyan,
                    disabledContainerColor = Color.White,
                )
            ) {
                Text("Expense")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text("Date", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier=Modifier.width(8.dp))
            CustomTextFieldComponent(
                date,
                onDateChange = { newDate -> date = newDate },
                placeholder = "yyyy-MM-dd HH:mm:ss",
            )
        }
        Spacer(modifier=Modifier.height(30.dp))
        Row {
            Text("Title", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier=Modifier.width(8.dp))
            CustomTextFieldComponent(
                title,
                onDateChange = { newDate -> title = newDate },
            )
        }
        Spacer(modifier=Modifier.height(30.dp))

        Row {
            Text("Amount", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier=Modifier.width(8.dp))
            CustomTextFieldComponent(
                amount,
                onDateChange = { newDate -> amount = newDate },
            )
        }
        Spacer(modifier=Modifier.height(30.dp))

        Row {
            Text("Category", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier=Modifier.width(8.dp))
            CustomTextFieldComponent(
                category,
                onDateChange = { newDate -> category = newDate },
            )
        }
        Spacer(modifier=Modifier.height(30.dp))

        Row {
            Text("Account", color = Color.Black,  modifier = Modifier.width(100.dp))
            Spacer(modifier=Modifier.width(8.dp))
            CustomTextFieldComponent(
                account,
                onDateChange = { newDate -> account = newDate },
            )
        }
        Spacer(modifier=Modifier.height(30.dp))
        Row {
            CustomTextFieldComponent(description, onDateChange = { newDate -> description = newDate }, placeholder = "Description", {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                    val uri = createImageUri(context)
                    imageUri = uri
                    launcher.launch(uri)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            })
        }
        if(pass) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUri) // content URI
                    .crossfade(true)
                    .build(),
                contentDescription = "Captured Image"
            )
        }
    }
}