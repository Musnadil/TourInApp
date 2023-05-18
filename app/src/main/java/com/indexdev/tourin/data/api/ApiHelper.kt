package com.indexdev.tourin.data.api

import com.indexdev.tourin.data.model.request.*

class ApiHelper(private val apiService: ApiService) {
    suspend fun authRegister(registerRequest: RegisterRequest) = apiService.authRegister(registerRequest)
    suspend fun authLogin(loginRequest: LoginRequest) = apiService.authLogin(loginRequest)
    suspend fun getTourList() = apiService.getTourList()
    suspend fun getPoiById(id:Int) = apiService.getPoiById(id)
    suspend fun postRate(rateRequest: RateRequest) = apiService.postRate(rateRequest)
    suspend fun editUsername(id:Int, updateUserRequest: UpdateUserRequest) = apiService.editUsername(id, updateUserRequest)
    suspend fun getRecommendationList() = apiService.getRecommendationList()
    suspend fun getAllUserMitra() = apiService.getAllUserMitra()
    suspend fun getUserPartnerById(id: Int) = apiService.getUserPartnerById(id)
    suspend fun getProductByPartnerId(id: Int) = apiService.getProductByPartnerId(id)
    suspend fun getProductById(id: Int) = apiService.getProductById(id)
    suspend fun forgotPassword(email: RequestEmailCheck) = apiService.forgotPassword(email)
    suspend fun verifyOtp(requestVerifyOtp: RequestVerifyOtp) = apiService.verifyOtp(requestVerifyOtp)
    suspend fun newPassword(requestNewPassword: RequestNewPassword) = apiService.newPassword(requestNewPassword)
}