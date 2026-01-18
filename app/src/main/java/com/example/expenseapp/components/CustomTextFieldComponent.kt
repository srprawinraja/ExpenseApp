package com.example.expenseapp.components
import android.util.Log
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expenseapp.R

@Composable
fun CustomTextFieldComponent(
    text: String,
    placeholder: String = "",
    disable: Boolean = false,
    onDataChange: (String) -> Unit,
    onTextFieldClicked: () -> Unit = {},
    onImgClick: (() -> Unit)? = null
) {
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
                    if(disable) {
                        onTextFieldClicked()
                    }
                }),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                BasicTextField(
                    value = text,
                    onValueChange = { it -> onDataChange(it) },
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    singleLine = onImgClick == null,
                    modifier = Modifier.weight(1f),
                    enabled = !disable
                )
                if(!disable) {
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

    }
}