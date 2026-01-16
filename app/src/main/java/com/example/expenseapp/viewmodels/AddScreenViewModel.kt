package com.example.expenseapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.api.RetroFitInstance
import com.example.expenseapp.data.api.accountType.AccountType
import com.example.expenseapp.data.api.accountType.toBottomUiList
import com.example.expenseapp.data.api.category.Category
import com.example.expenseapp.data.api.category.toBottomUiList
import com.example.expenseapp.data.ui.BottomSheetItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AddScreenViewModel: ViewModel(){
    private val TAG = "AddScreenViewModel"
    private val apiService = RetroFitInstance.apiServiceGetInstance
    private val _bottomScreenUiState = MutableStateFlow<NetworkResponse<List<BottomSheetItem>>>(NetworkResponse.Empty)
    val bottomScreenUiState: MutableStateFlow<NetworkResponse<List<BottomSheetItem>>> = _bottomScreenUiState

    fun getAllCategory(){
        viewModelScope.launch {
            try {
                _bottomScreenUiState.value = NetworkResponse.Loading
                val response = apiService.getAllCategory()
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data!=null) {
                        _bottomScreenUiState.value = NetworkResponse.Success(data.toBottomUiList(data))
                    } else {
                        Log.d(TAG, "unsuccessful request "+"body is null")
                    }
                } else {
                    _bottomScreenUiState.value = NetworkResponse.Error(response.message())
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "exception occurred when getting category ${e}")
            }
        }
    }
    fun getAllAccountType(){
        viewModelScope.launch {
            try {
                _bottomScreenUiState.value = NetworkResponse.Loading
                val response = apiService.getAllAccountType()
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data!=null) {
                        _bottomScreenUiState.value = NetworkResponse.Success(data.toBottomUiList(data))
                    } else {
                        Log.d(TAG, "unsuccessful request "+"body is null")
                    }
                } else {
                    _bottomScreenUiState.value = NetworkResponse.Error(response.message())
                    Log.d(TAG, "unsuccessful request "+response.code()+" "+response.message().toString())
                }
            } catch (e: Exception){
                Log.d(TAG, "exception occurred when getting category ${e}")
            }
        }
    }
}