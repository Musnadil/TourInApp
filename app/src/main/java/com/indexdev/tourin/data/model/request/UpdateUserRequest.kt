package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("username")
    val username: String
)