package com.indexdev.tourin.data.model.request

import com.google.gson.annotations.SerializedName

class LoginRequest (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)