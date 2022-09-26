package com.indexdev.tourin.data.model.request

import com.google.gson.annotations.SerializedName

class RegisterRequest (
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)