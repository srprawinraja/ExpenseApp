package com.example.expenseapp.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.api.RetroFitInstance
import com.example.expenseapp.data.request.api.record.Note
import com.example.expenseapp.data.request.api.record.RecordRequest
import com.example.expenseapp.data.response.api.accountType.toBottomUiList
import com.example.expenseapp.data.response.api.category.toBottomUiList
import com.example.expenseapp.data.ui.BottomSheetItem
import com.example.expenseapp.utils.FileUri.fileToMultipart
import com.example.expenseapp.utils.FileUri.uriToFile
import com.example.expenseapp.utils.isInt
import com.example.expenseapp.utils.isValidDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddScreenViewModel : ViewModel() {
    private val TAG = "AddScreenViewModel"
    private val apiService = RetroFitInstance.apiServiceGetInstance
    private val _bottomScreenUiState =
        MutableStateFlow<NetworkResponse<List<BottomSheetItem>>>(NetworkResponse.Empty)
    val bottomScreenUiState: MutableStateFlow<NetworkResponse<List<BottomSheetItem>>> =
        _bottomScreenUiState

    var error = MutableStateFlow<String?>(null)

    fun getAllCategory() {
        viewModelScope.launch {
            try {
                _bottomScreenUiState.value = NetworkResponse.Loading
                val response = apiService.getAllCategory()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _bottomScreenUiState.value =
                            NetworkResponse.Success(data.toBottomUiList(data))
                    } else {
                        Log.d(TAG, "unsuccessful request " + "body is null")
                    }
                } else {
                    _bottomScreenUiState.value = NetworkResponse.Error(response.message())
                    Log.d(TAG,
                        "unsuccessful request " + response.code() + " " + response.message()
                            .toString()
                    )
                    error.value = "failed to fetch categories"
                }
            } catch (e: Exception) {
                Log.d(TAG, "exception occurred when getting category ${e}")
                error.value = "failed to fetch categories"
            }
        }
    }

    fun getAllAccountType() {
        viewModelScope.launch {
            try {
                _bottomScreenUiState.value = NetworkResponse.Loading
                val response = apiService.getAllAccountType()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _bottomScreenUiState.value =
                            NetworkResponse.Success(data.toBottomUiList(data))
                    } else {
                        Log.d(TAG, "unsuccessful request " + "body is null")
                        error.value = "failed to fetch account types"
                    }
                } else {
                    _bottomScreenUiState.value = NetworkResponse.Error(response.message())
                    error.value = "failed to fetch account types"
                    Log.d(TAG,
                        "unsuccessful request " + response.code() + " " + response.message()
                            .toString()

                    )
                }
            } catch (e: Exception) {
                Log.d(TAG, "exception occurred when getting account types ${e}")
                error.value = "failed to fetch account types"
            }
        }
    }
    fun clearError(){
        error.value = ""
    }
    fun saveTheDetail(
        date: String,
        title: String,
        amount: String,
        category: String,
        account: String,
        context: Context,
        description: String,
        uris: List<Uri>
    ) {
        viewModelScope.launch {
            try {
                if (title.isEmpty() || date.isEmpty() || amount.isEmpty() || category.isEmpty() || account.isEmpty()) {
                    Log.d("values are", title+" "+date+" "+amount+" "+category+" "+account)
                    error.value = "Please fill in all details"
                }
                else if (!amount.all { it.isDigit() } || amount.toInt() < 0) {
                    error.value = "Amount must be a valid positive number"
                }
                else if (!isValidDate(date)) {
                    error.value = "Please enter a valid date"
                }
                else {
                    val keys = mutableListOf<String>()

                    if(!uris.isEmpty()) {
                        val files = uriToFile(context, uris)
                        val parts = fileToMultipart(files)

                        val response = apiService.uploadImage(parts)
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data != null) {
                                for (url in data.urls) {
                                    keys.add(url.key)
                                }
                            } else {
                                error.value = "failed to upload image"
                            }
                        } else {
                            error.value = "failed to upload image"
                        }
                    }
                    val response  = apiService.createRecord(RecordRequest(
                        accountType = account,
                        amount = amount.toInt(),
                        category = category,
                        date = date,
                        note = Note(
                            content = description,
                            keys = keys
                        ),
                        title = title
                    ))
                    Log.d(TAG, response.toString())
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Failed: ${e.message}")
                error.value = "failed to save"
            }
        }
    }

}