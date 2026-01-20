package com.example.expenseapp.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.api.RetroFitInstance
import com.example.expenseapp.data.request.api.record.Note
import com.example.expenseapp.data.request.api.record.RecordRequest
import com.example.expenseapp.data.request.api.record.toUtcDateString
import com.example.expenseapp.data.response.api.accountType.toBottomUiList
import com.example.expenseapp.data.response.api.category.toBottomUiList
import com.example.expenseapp.data.ui.BottomSheetItem
import com.example.expenseapp.data.ui.File
import com.example.expenseapp.utils.FileUri.fileToMultipart
import com.example.expenseapp.utils.FileUri.uriToFile
import com.example.expenseapp.utils.getAsDate
import com.example.expenseapp.utils.isValidDate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddScreenViewModel : ViewModel() {
    private val TAG = "AddScreenViewModel"

    var date by mutableStateOf(
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )

    var title by mutableStateOf("")
    var amount by mutableStateOf("")
    var description by mutableStateOf("")

    var isIncome by mutableStateOf(true)

    var categoryName by mutableStateOf("")
    var categoryId by mutableStateOf("")

    var accountName by mutableStateOf("")
    var accountId by mutableStateOf("")

    var uploadImage = mutableStateListOf<File>()
    var navigateHome by mutableStateOf(false)
    private val keys = mutableListOf<String>()
    private val _bottomScreenUiState =
        MutableStateFlow<NetworkResponse<List<BottomSheetItem>>>(NetworkResponse.Empty)
    val bottomScreenUiState: MutableStateFlow<NetworkResponse<List<BottomSheetItem>>> =
        _bottomScreenUiState

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val apiService = RetroFitInstance.apiServiceGetInstance


    fun reset() {
        date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        title = ""
        amount =""
        description = ""
        isIncome = true
        categoryName = ""
        categoryId = ""
        accountName = ""
        accountId = ""
        uploadImage.clear()
        keys.clear()
    }

    fun getAllCategory(isIncome: Boolean) {
        viewModelScope.launch {
            try {
                _bottomScreenUiState.value = NetworkResponse.Loading
                val response = apiService.getAllCategory(isIncome)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _bottomScreenUiState.value =
                            NetworkResponse.Success(data.toBottomUiList())
                    } else {
                        sendMessage("no category found")
                        Log.d(TAG, "GetCategory failed: response body was null")
                    }
                } else {
                    sendMessage("failed to get category data")
                    Log.d(TAG, "GetCategory failed: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                sendMessage("failed to get category data")
                Log.d(TAG, "GetCategory failed: ${e}")
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
                            NetworkResponse.Success(data.toBottomUiList())
                    } else {
                        sendMessage("no account type found")
                        Log.d(TAG, "GetAccount failed: body is null")
                    }
                } else {
                    sendMessage("failed to get account type")
                    Log.d(TAG, "GetAccount failed: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                sendMessage("failed to get account type")
                Log.d(TAG, "GetAccount failed: ${e}")
            }
        }
    }

    fun saveTheDetail(context: Context) {
        viewModelScope.launch {
            try {
                if (title.isEmpty() || date.isEmpty() || amount.isEmpty() || categoryName.isEmpty() || accountName.isEmpty()) {
                    sendMessage("Please fill in all details")
                } else if (!amount.all { it.isDigit() } || amount.toInt() < 0) {
                    sendMessage("Amount must be a valid positive number")
                } else if (!isValidDate(date)) {
                    sendMessage("Please enter a valid date")
                } else {
                    sendMessage("saving please wait...")

                    val files = uriToFile(context, uploadImage)
                    val parts = fileToMultipart(files)

                    if (!parts.isEmpty()) {
                        val response = apiService.uploadImage(parts)
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data != null) {
                                for (key in data.keys) {
                                    keys.add(key)
                                }
                            }
                        } else {
                            sendMessage("failed to upload image")
                            Log.d(TAG, "Upload failed: ${response.errorBody()}")
                        }
                    }
                    val recordRequest = RecordRequest(
                        accountType = accountId,
                        amount = amount.toInt(),
                        category = categoryId,
                        date = date,
                        note = Note(
                            content = description,
                            keys = keys
                        ),
                        title = title
                    )
                    recordRequest.toUtcDateString(getAsDate(date))

                    val response = apiService.createRecord(
                        recordRequest
                    )
                    if(response.isSuccessful) {
                        sendMessage("saved successfully")
                        navigateHome = true
                    } else {
                        sendMessage("failed to save detail")
                        Log.d(TAG, "SaveDetail failed: ${response.errorBody()}")
                    }

                }
            } catch (e: Exception) {
                sendMessage("failed to save detail")
                Log.d(TAG, "SaveDetail failed: ${e}")
            }
        }
    }
    fun getRecordDetail(id: String){
        viewModelScope.launch {
            try {
                val response = apiService.getRecordDetail(id)
                if(response.isSuccessful){
                    val data = response.body()
                    if(data!=null) {
                        date = data.date.substring(0, 10)
                        title = data.title
                        amount = data.amount.toString()
                        description = data.note.content
                        isIncome = data.category.isIncome
                        categoryName = data.category.categoryName
                        categoryId = data.category._id
                        accountName = data.accountType.accountTypeName
                        accountId = data.accountType._id
                        for(url in data.note.urls){
                            uploadImage.add(File(
                                url = url
                            ))
                        }
                        for(key in data.note.keys){
                            keys.add(key)
                        }
                    }
                } else {
                    sendMessage("failed to get record detail")
                    Log.d(TAG, "RecordDetail failed: ${response.errorBody()}")
                }
            }  catch (e: Exception) {
                sendMessage("failed to get record detail")
                Log.d(TAG, "RecordDetail failed: ${e}")
            }
        }
    }

    fun deleteRecord(id: String){
        viewModelScope.launch {
            try {
                sendMessage("deleting please wait...")

                val response = apiService.deleteRecord(id)
                if(response.isSuccessful){
                    sendMessage("deleted successfully")
                    navigateHome = true
                } else {
                    sendMessage("failed to delete")
                    Log.d(TAG, "Delete failed: ${response.errorBody()}")
                }
            }  catch (e: Exception) {
                sendMessage("failed to delete")
                Log.d(TAG, "Delete failed: ${e}")
            }
        }
    }

    fun updateRecord(context: Context, id: String){
        viewModelScope.launch {
            try {

                if (title.isEmpty() || date.isEmpty() || amount.isEmpty() || categoryName.isEmpty() || accountName.isEmpty()) {
                    sendMessage("Please fill in all details")
                } else if (!amount.all { it.isDigit() } || amount.toInt() < 0) {
                    sendMessage("Amount must be a valid positive number")
                } else if (!isValidDate(date)) {
                    sendMessage("Please enter a valid date")
                } else {
                    sendMessage("updating please wait...")
                    val files = uriToFile(context, uploadImage)
                    val parts = fileToMultipart(files)
                    if (!parts.isEmpty()) {
                        val response = apiService.uploadImage(parts)
                        if (response.isSuccessful) {
                            val data = response.body()
                            if (data != null) {
                                for (key in data.keys) {
                                    keys.add(key)
                                }
                            } else {
                                sendMessage("failed upload update")
                                Log.d(TAG, "UpdateUpload failed: ${response.message()}")
                            }
                        } else {
                            sendMessage("failed upload update")
                            Log.d(TAG, "UpdateUpload failed: ${response.errorBody()}")
                        }
                    }
                    val recordRequest = RecordRequest(
                        accountType = accountId,
                        amount = amount.toInt(),
                        category = categoryId,
                        date = date,
                        note = Note(
                            content = description,
                            keys = keys
                        ),
                        title = title
                    )
                    recordRequest.toUtcDateString(getAsDate(date))
                    val response = apiService.updateRecord(
                        recordRequest = recordRequest,
                        id = id
                    )
                    if (response.isSuccessful) {
                        sendMessage("saved successfully")
                        navigateHome = true
                    } else {
                        sendMessage("failed to update")
                        Log.d(TAG, "Update failed: ${response.errorBody()}")
                    }
                }
            }  catch (e: Exception) {
                sendMessage("failed to update")
                Log.d(TAG, "Update failed: ${e}")
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            _message.emit(text)
        }
    }
}