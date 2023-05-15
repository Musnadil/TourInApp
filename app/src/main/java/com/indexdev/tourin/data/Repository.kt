package com.indexdev.tourin.data

import com.indexdev.tourin.data.api.ApiHelper
import com.indexdev.tourin.data.model.request.LoginRequest
import com.indexdev.tourin.data.model.request.RateRequest
import com.indexdev.tourin.data.model.request.RegisterRequest
import com.indexdev.tourin.data.model.request.UpdateUserRequest
import okhttp3.RequestBody

class Repository(private val apiHelper: ApiHelper) {
    suspend fun authRegister(registerRequest: RegisterRequest) = apiHelper.authRegister(registerRequest)
    suspend fun authLogin(loginRequest: LoginRequest) = apiHelper.authLogin(loginRequest)
    suspend fun getTourList() = apiHelper.getTourList()
    suspend fun getPoiById(id:Int) = apiHelper.getPoiById(id)
    suspend fun postRate(rateRequest: RateRequest) = apiHelper.postRate(rateRequest)
    suspend fun editUsername(id:Int, updateUserRequest: UpdateUserRequest) = apiHelper.editUsername(id, updateUserRequest)
    suspend fun getRecommendationList() = apiHelper.getRecommendationList()
    suspend fun getAllUserMitra() = apiHelper.getAllUserMitra()
    suspend fun getUserPartnerById(id: Int) = apiHelper.getUserPartnerById(id)

}