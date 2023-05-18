package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class RequestVerifyOtp(
    @SerializedName("email_pemilik")
    val emailPemilik: String,
    @SerializedName("otp_code")
    val otpCode: Int
)