package com.indexdev.tourin.data.model.request

import com.google.gson.annotations.SerializedName

data class RequestEmailCheck(
    @SerializedName("email")
    val email: String
)