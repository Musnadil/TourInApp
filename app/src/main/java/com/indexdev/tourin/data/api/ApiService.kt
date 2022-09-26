package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.response.ResponseLogin
import com.indexdev.tourin.data.model.response.ResponseRegister
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun authRegister(@Body registerRequest: RegisterRequest): ResponseRegister

    @POST("login")
    suspend fun authLogin(@Body loginRequest: LoginRequest): ResponseLogin

//    @GET("wisata")
//    suspend fun getTour()
}