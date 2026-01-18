package com.example.expenseapp.api

sealed class NetworkResponse<out T>() {
    data class Success<out T>(val data:T):NetworkResponse<T>()
    object Loading: NetworkResponse<Nothing>()
    object Empty: NetworkResponse<Nothing>()
}