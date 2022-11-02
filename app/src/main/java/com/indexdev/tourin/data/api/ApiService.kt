package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.response.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
}