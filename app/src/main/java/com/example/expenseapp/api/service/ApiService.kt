package com.example.expenseapp.api.service

import com.example.expenseapp.data.request.api.record.RecordRequest
import com.example.expenseapp.data.response.api.accountType.AccountType
import com.example.expenseapp.data.response.api.category.Category
import com.example.expenseapp.data.response.api.record.date.DateSummary
import com.example.expenseapp.data.response.api.record.detail.RecordResponse
import com.example.expenseapp.data.response.api.record.month.MonthSummary
import com.example.expenseapp.data.response.api.upload.Keys
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("category")
    suspend fun getAllCategory(@Query("isIncome") isIncome: Boolean): Response<Category>
    @GET("accountType")
    suspend fun getAllAccountType(): Response<AccountType>

    @GET("record/month")
    suspend fun getMonthSummary(@Query("year") year: Int, @Query("month") month: Int): Response<MonthSummary>


    @GET("record/date")
    suspend fun getDateSummary(@Query("date") date: String, @Query("page")  page: Int, @Query("limit") limit: Int): Response<DateSummary>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part files: List<MultipartBody.Part>
    ): Response<Keys>

    @POST("record")
    suspend fun createRecord(
        @Body recordRequest: RecordRequest
    ): Response<Unit>

    @GET("record")
    suspend fun getRecordDetail(
        @Query("id") id: String
    ): Response<RecordResponse>

    @DELETE("record")
    suspend fun deleteRecord(
        @Query("id") id: String
    ): Response<Unit>

    @PATCH("record")
    suspend fun updateRecord(
        @Body recordRequest: RecordRequest,
        @Query("id") id: String
    ): Response<Unit>

}