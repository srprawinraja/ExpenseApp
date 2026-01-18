package com.example.expenseapp.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expenseapp.api.NetworkResponse
import com.example.expenseapp.api.RetroFitInstance
import com.example.expenseapp.data.response.api.category.toBottomUiList
import com.example.expenseapp.data.response.api.record.date.DateSummary
import com.example.expenseapp.data.response.api.record.month.MonthSummary
import com.example.expenseapp.data.ui.BottomSheetItem
import com.example.expenseapp.db.record.RecordRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class HomeScreenViewModel: ViewModel() {

    private val TAG = "HomeScreenViewModel"
    private var page: Int = 1
    private var limit: Int = 100
    private val _currentMonth = mutableStateOf(YearMonth.now())

    private var _currentMonthName = mutableStateOf(getCurrentMonthFormatted())

    private var _monthIncome by mutableStateOf("0")
    val monthIncome: String
        get() = _monthIncome
    private var _monthExpense by mutableStateOf("0")
    val monthExpense: String
            get() = _monthExpense
    private var _monthTotal by mutableStateOf("0")
    val monthTotal: String
        get() = _monthTotal
    val currentMonthName: MutableState<String> = _currentMonthName
    private val apiService = RetroFitInstance.apiServiceGetInstance
    private val _monthUiState =
        MutableStateFlow<NetworkResponse<MonthSummary>>(NetworkResponse.Empty)
    val monthUiState: MutableStateFlow<NetworkResponse<MonthSummary>> =
        _monthUiState
    private val _dateUiState =
        MutableStateFlow<NetworkResponse<DateSummary>>(NetworkResponse.Empty)
    val dateUiState: MutableStateFlow<NetworkResponse<DateSummary>> =
        _dateUiState

    fun reset(){
        _monthIncome = "0"
        _monthExpense = "0"
        _monthTotal = "0"
        _monthUiState.value = NetworkResponse.Empty
        _dateUiState.value = NetworkResponse.Empty
    }

    fun previousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
        _currentMonthName.value = getCurrentMonthFormatted()
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
        _currentMonthName.value = getCurrentMonthFormatted()
    }

    fun getCurrentMonthFormatted(): String {
        return _currentMonth.value.format(DateTimeFormatter.ofPattern("MMM"))
    }
    fun getAllMonthlyData(){
        viewModelScope.launch {
            try {
                _monthIncome = "0"
                _monthExpense = "0"
                _monthTotal = "0"
                _monthUiState.value = NetworkResponse.Empty
                val response = apiService.getMonthSummary(_currentMonth.value.year, _currentMonth.value.monthValue)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _monthIncome = data.income.toString()
                        _monthExpense = data.expense.toString()
                        _monthTotal = data.total.toString()
                        _monthUiState.value = NetworkResponse.Success(data)
                        for(dateDetail in data.dates) {
                            getAllDateData(dateDetail.date)
                        }
                    }
                } else {
                    Log.d(TAG, "getAllMonthlyData failed: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d(TAG, "getAllMonthlyData failed: ${e}")
            }
        }
    }
    fun getAllDateData(date: String){

        viewModelScope.launch {
            try {
                _dateUiState.value = NetworkResponse.Loading

                val response = apiService.getDateSummary(
                    date = date,
                    page = page,
                    limit = limit
                )
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        _dateUiState.value = NetworkResponse.Success(data)
                    } else {
                        Log.d(TAG, "getAllDateData failed: body is null")
                    }
                } else {
                    Log.d(TAG, "getAllDateData failed: ${response.errorBody()}")

                }
            } catch (e: Exception) {
                Log.d(TAG, "getAllDateData failed: ${e}")
            }
        }
    }

}