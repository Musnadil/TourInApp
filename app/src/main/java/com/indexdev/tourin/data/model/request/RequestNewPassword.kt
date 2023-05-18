package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class RequestNewPassword(
    @SerializedName("email")
    val emailPemilik: String,
    @SerializedName("password")
    val password: String
)