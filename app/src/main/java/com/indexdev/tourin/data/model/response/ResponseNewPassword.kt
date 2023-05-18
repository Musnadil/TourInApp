package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class ResponseNewPassword(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)