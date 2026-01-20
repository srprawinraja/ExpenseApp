package com.example.expenseapp.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
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
import com.example.expenseapp.data.ui.File
import com.example.expenseapp.utils.FileUri
import com.example.expenseapp.viewmodels.AddScreenViewModel

@Composable
fun AddScreen(
    navHostController: NavHostController,
    addScreenViewModel: AddScreenViewModel,
    id: String?
) {
    LaunchedEffect(Unit) {
        addScreenViewModel.reset()
        if(id!=null && id!="") {
            addScreenViewModel.getRecordDetail(id)
        }
    }

    val bottomUiData = addScreenViewModel.bottomScreenUiState.collectAsState().value
    var lastAddedImageUrl by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val onBottomItemClickState = remember { mutableStateOf<(String, String) -> Unit>({ _, _ -> }) }
    val showBottomScreen = remember {
        mutableStateOf(false)
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            addScreenViewModel.uploadImage.add(File(lastAddedImageUrl))
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

    /*
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                addScreenViewModel.reset()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }*/

    LaunchedEffect(Unit) {
        addScreenViewModel.message.collect { text ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
    if (addScreenViewModel.navigateHome) {
        LaunchedEffect(Unit) {
            addScreenViewModel.navigateHome = false
            navHostController.popBackStack()
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
                    addScreenViewModel.categoryName = ""
                    addScreenViewModel.categoryId = ""
                    addScreenViewModel.isIncome = !addScreenViewModel.isIncome
                },
                colors = ButtonColors(
                    containerColor = if (addScreenViewModel.isIncome) Color.Black
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
                    addScreenViewModel.categoryName = ""
                    addScreenViewModel.categoryId = ""
                    addScreenViewModel.isIncome = !addScreenViewModel.isIncome
                },
                colors = ButtonColors(
                    containerColor = if (addScreenViewModel.isIncome) Color.White
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
                addScreenViewModel.date,
                onDataChange = { newDate -> addScreenViewModel.date = newDate },
                placeholder = "yyyy-MM-dd",
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row {
            Text("Title", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                addScreenViewModel.title,
                onDataChange = { newTitle -> addScreenViewModel.title = newTitle },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Amount", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                addScreenViewModel.amount,
                onDataChange = { newAmount -> addScreenViewModel.amount = newAmount },
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Category", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                addScreenViewModel.categoryName,
                onDataChange = { newCategory -> addScreenViewModel.categoryName = newCategory },
                onTextFieldClicked = {
                    onBottomItemClickState.value = { id, name ->
                        addScreenViewModel.categoryName = name
                        addScreenViewModel.categoryId = id
                    }
                    showBottomScreen.value = true
                    addScreenViewModel.getAllCategory(addScreenViewModel.isIncome)
                },
                disable = true,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Text("Account", color = Color.Black, modifier = Modifier.width(100.dp))
            Spacer(modifier = Modifier.width(8.dp))
            CustomTextFieldComponent(
                addScreenViewModel.accountName,
                onDataChange = { newAccount -> addScreenViewModel.accountName = newAccount },
                onTextFieldClicked = {
                    onBottomItemClickState.value = { id, name ->
                        addScreenViewModel.accountName = name
                        addScreenViewModel.accountId = id
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
                addScreenViewModel.description,
                onDataChange = { newDescription ->
                    addScreenViewModel.description = newDescription
                },
                placeholder = "Description",
                onImgClick = {
                    if (addScreenViewModel.uploadImage.size < 3) {
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
            addScreenViewModel.uploadImage.forEach { file ->

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(file.url?: file.uri)
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
        if(id!=null && id=="") {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                onClick = {
                    addScreenViewModel.saveTheDetail(
                        context = context
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
        } else {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp).weight(1f),
                    onClick = {
                        addScreenViewModel.updateRecord(
                            context = context,
                            id = id!!
                        )
                    },
                    colors = ButtonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.Green,
                    )
                ) {
                    Text("Update", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp).weight(1f),
                    onClick = {
                        addScreenViewModel.deleteRecord(
                            id = id!!
                        )
                    },
                    colors = ButtonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black,
                        disabledContainerColor = Color.Green,
                    )
                ) {
                    Text("Delete", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
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
