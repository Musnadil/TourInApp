package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RegisterRequest

class ApiHelper(private val apiService: ApiService) {
    suspend fun authRegister(registerRequest: RegisterRequest) = apiService.authRegister(registerRequest)
    suspend fun authLogin(loginRequest: LoginRequest) = apiService.authLogin(loginRequest)
    suspend fun getTourList() = apiService.getTourList()
    suspend fun getPoiById(id:Int) = apiService.getPoiById(id)
}