package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class ResponseRecommendation(
    @SerializedName("kode_user")
    val kodeUser: String,
    @SerializedName("kode_wisata")
    val kodeWisata: String,
    @SerializedName("wisata")
    val wisata: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("longi")
    val longi: String,
    @SerializedName("url_image")
    val urlImage: String,
    @SerializedName("rating")
    val rating: String
)