package com.example.expenseapp.api.service

import com.example.expenseapp.data.request.api.record.RecordRequest
import com.example.expenseapp.data.response.api.accountType.AccountType
import com.example.expenseapp.data.response.api.category.Category
import com.example.expenseapp.data.response.api.record.RecordResponse
import com.example.expenseapp.data.response.api.upload.Urls
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("category")
    suspend fun getAllCategory(): Response<Category>
    @GET("accountType")
    suspend fun getAllAccountType(): Response<AccountType>
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part files: List<MultipartBody.Part>
    ): Response<Urls>

    @POST("record")
    suspend fun createRecord(
        @Body recordRequest: RecordRequest
    ): Response<RecordResponse>

}