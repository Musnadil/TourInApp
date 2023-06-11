package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class ResponseListRateFacility(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: List<Data>
)