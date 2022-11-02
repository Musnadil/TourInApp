package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class RateRequest(
    @SerializedName("id_rate")
    val idRate: Any?,
    @SerializedName("kode_user")
    val kodeUser: String,
    @SerializedName("kode_wisata")
    val kodeWisata: String,
    @SerializedName("rate_value")
    val rateValue: String,
    @SerializedName("time")
    val time: Any?
)