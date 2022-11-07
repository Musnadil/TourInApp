package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import okhttp3.RequestBody

class ApiHelper(private val apiService: ApiService) {
    suspend fun authRegister(registerRequest: RegisterRequest) = apiService.authRegister(registerRequest)
    suspend fun authLogin(loginRequest: LoginRequest) = apiService.authLogin(loginRequest)
    suspend fun getTourList() = apiService.getTourList()
    suspend fun getPoiById(id:Int) = apiService.getPoiById(id)
    suspend fun postRate(rateRequest: RateRequest) = apiService.postRate(rateRequest)
    suspend fun editUsername(id:Int, updateUserRequest: UpdateUserRequest) = apiService.editUsername(id, updateUserRequest)
    suspend fun getRecommendationList() = apiService.getRecommendationList()

}