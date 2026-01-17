package com.example.expenseapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.expenseapp.components.BottomSheetSelector
import com.example.expenseapp.components.CustomTextFieldComponent
import com.example.expenseapp.utils.FileUri
import com.example.expenseapp.viewmodels.AddScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddScreen(navHostController: NavHostController, addScreenViewModel: AddScreenViewModel) {

    val bottomUiData = addScreenViewModel.bottomScreenUiState.collectAsState().value
    val error = addScreenViewModel.error.collectAsState().value

    val context = LocalContext.current
    val current = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formatted = current.format(formatter)
    val date = remember { mutableStateOf(formatted) }
    val title = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    var isIncomeButtonClicked by remember { mutableStateOf(true) }
    val categoryName = remember { mutableStateOf("") }
    val accountName =  remember { mutableStateOf("") }
    val categoryId = remember { mutableStateOf("") }
    val accountId = remember { mutableStateOf("") }
    val onBottomItemClickState = remember { mutableStateOf<(String, String) -> Unit>({ _, _ -> }) }

    val showBottomScreen = remember {
        mutableStateOf(false)
    }
    var lastAddedImageUrl by remember { mutableStateOf<Uri?>(null) }
    val uploadedUrls = remember { mutableStateListOf<Uri>() }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            uploadedUrls.add(lastAddedImageUrl!!)
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = FileUri.createImageUri(context)
            lastAddedImageUrl = uri
            launcher.launch(uri)
        } else {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

        LaunchedEffect(error) {
            if (error!=null && error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                addScreenViewModel.clearError()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                    navHostController.navigate("Home")
                },
                colors = ButtonColors(
                    containerColor = if (isIncomeButtonClicked) Color.Black
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
                    containerColor = if (isIncomeButtonClicked) Color.White
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
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                date,
                onDateChange = { newDate -> date.value = newDate },
                placeholder = "dd-MM-yyyy",
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Text("Title", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                title,
                onDateChange = { newTitle -> title.value = newTitle },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Amount", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                amount,
                onDateChange = { newAmount -> amount.value = newAmount },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Category", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                categoryName,
                onDateChange = { newCategory -> categoryName.value = newCategory },
                onTextFieldClicked = {
                    onBottomItemClickState.value = { id, name ->
                        categoryName.value = name
                        categoryId.value = id
                        Log.d("updated the values", "updated");
                    }
                    showBottomScreen.value = true
                    addScreenViewModel.getAllCategory()
                },
                disable = true,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Account", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                accountName,
                onDateChange = { newAccount -> accountName.value = newAccount },
                onTextFieldClicked = {
                    onBottomItemClickState.value = { id, name ->
                        accountName.value = name
                        accountId.value = id
                    }
                    showBottomScreen.value = true
                    addScreenViewModel.getAllAccountType()
                },
                disable = true,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            CustomTextFieldComponent(
                description,
                onDateChange = { newDate -> description.value = newDate },
                placeholder = "Description",
                onImgClick = {
                    if (uploadedUrls.size <= 3) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED
                        ) {
                            val uri = FileUri.createImageUri(context)
                            lastAddedImageUrl = uri
                            launcher.launch(uri)
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    } else {
                        Toast.makeText(context, "uploading image limit reached", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uploadedUrls.forEach { url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {
                addScreenViewModel.saveTheDetail(
                    date = date.value,
                    title = title.value,
                    amount = amount.value,
                    category = categoryId.value,
                    account = accountId.value,
                    context = context,
                    description = description.value,
                    uris = uploadedUrls,
                )
            },
            colors = ButtonColors(
                containerColor = Color.Green,
                contentColor = Color.Black,
                disabledContentColor = Color.Black,
                disabledContainerColor = Color.Green,
            )
        ) {
            Text("Add", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
    if (showBottomScreen.value) {
        BottomSheetSelector(
            uiDate = bottomUiData,
            onBottomItemClickState,
            {
                showBottomScreen.value = false
            }
        )
    }
}
