package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id_rate_fasilitas")
    val idRateFasilitas: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("id_fasilitas")
    val idFasilitas: String,
    @SerializedName("nama_fasilitas")
    val namaFasilitas: String,
    @SerializedName("nama_wisata")
    val namaWisata: String,
    @SerializedName("rate_value")
    val rateValue: String?,
    @SerializedName("time")
    val time: String
)