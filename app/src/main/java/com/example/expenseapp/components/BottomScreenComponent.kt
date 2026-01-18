package com.example.expenseapp.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.data.ui.BottomSheetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSelector(
    uiDate: NetworkResponse<List<BottomSheetItem>>,
    onItemSelectedState: MutableState<(String, String) -> Unit>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            when (uiDate) {
                is NetworkResponse.Success -> {
                    uiDate.data.forEach { item ->
                        Text(
                            text = item.itemName,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemSelectedState.value(item.id, item.itemName)
                                    onDismiss()
                                }
                                .padding(16.dp)
                        )
                        Divider()
                    }
                }

                is NetworkResponse.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        CircularProgressIndicator()
                    }
                }

                else -> {

                }
            }
        }

    }
}
