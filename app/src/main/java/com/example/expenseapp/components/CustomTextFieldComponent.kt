package com.example.expenseapp.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenseapp.R
import com.example.expenseapp.utils.createImageUri

@Composable
fun CustomTextFieldComponent(
    text: String,
    onDateChange: (String) -> Unit,
    placeholder: String = "",
    onImgClick: (() -> Unit)? = null,
    disable: Boolean = false
) {
    var showBottomScreen by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth().clickable(onClick = {
                    showBottomScreen = true
                }),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                if(!disable) {
                    BasicTextField(
                        value = text,
                        onValueChange = { it -> onDateChange(it) },
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                        singleLine = onImgClick == null,
                        modifier = Modifier.weight(1f),

                    )
                    if (onImgClick != null) {
                        Icon(
                            painter = painterResource(R.drawable.icon_camera),
                            contentDescription = "button",
                            modifier = Modifier.height(20.dp).width(20.dp).clickable(
                                onClick = {
                                    onImgClick()
                                }
                            )
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )
        if(showBottomScreen){
            BottomSheetSelector(mutableListOf("apple", "banna"), {}, {
                showBottomScreen = !showBottomScreen
            })
        }
    }
}