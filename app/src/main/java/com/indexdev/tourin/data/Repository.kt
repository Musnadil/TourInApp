package com.indexdev.tourin.data

import com.indexdev.tourin.data.api.ApiHelper
import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RegisterRequest

class Repository(private val apiHelper: ApiHelper) {
    suspend fun authRegister(registerRequest: RegisterRequest) = apiHelper.authRegister(registerRequest)
    suspend fun authLogin(loginRequest: LoginRequest) = apiHelper.authLogin(loginRequest)
    suspend fun getTourList() = apiHelper.getTourList()
}