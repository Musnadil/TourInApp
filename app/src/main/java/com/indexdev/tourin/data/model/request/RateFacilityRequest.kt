package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class RateFacilityRequest(
    @SerializedName("rate_value")
    val rateValue: Float
)