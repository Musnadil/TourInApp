package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class ResponseTourList(
    @SerializedName("id_wisata")
    val idWisata: String,
    @SerializedName("wisata")
    val wisata: String,
    @SerializedName("deskripsi")
    val deskripsi: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("longi")
    val longi: String,
    @SerializedName("url_image")
    val urlImage: String,
    @SerializedName("rating")
    val rating: String
)