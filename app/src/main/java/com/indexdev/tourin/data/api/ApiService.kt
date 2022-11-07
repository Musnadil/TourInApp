package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import com.indexdev.tourin.data.model.response.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun authRegister(@Body registerRequest: RegisterRequest): ResponseRegister

    @POST("login")
    suspend fun authLogin(@Body loginRequest: LoginRequest): ResponseLogin

    @GET("wisata")
    suspend fun getTourList(): List<ResponseTourList>

    @GET("poi/{id}")
    suspend fun getPoiById(@Path("id") id: Int): List<ResponsePOI>

    @POST("rate")
    suspend fun postRate(@Body rateRequest: RateRequest): ResponseRate

    @PUT("edit/{id}")
    suspend fun editUsername(
        @Path("id") id: Int,
        @Body newUsername: UpdateUserRequest
    ): ResponseUpdateUsername

    @GET("rekomendasi")
    suspend fun getRecommendationList() : List<ResponseRecommendation>
}