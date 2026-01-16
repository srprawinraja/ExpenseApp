package com.example.expenseapp.api.service

import com.example.expenseapp.data.api.accountType.AccountType
import com.example.expenseapp.data.api.category.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("category")
    suspend fun getAllCategory(): Response<Category>
    @GET("accountType")
    suspend fun getAllAccountType(): Response<AccountType>
}