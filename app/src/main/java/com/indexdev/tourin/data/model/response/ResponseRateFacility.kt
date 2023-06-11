package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class ResponseRateFacility(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)