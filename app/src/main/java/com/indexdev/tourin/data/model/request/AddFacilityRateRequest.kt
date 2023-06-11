package com.indexdev.tourin.data.model.request


import com.google.gson.annotations.SerializedName

data class AddFacilityRateRequest(
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("id_fasilitas")
    val idFasilitas: String,
    @SerializedName("nama_fasilitas")
    val namaFasilitas: String,
    @SerializedName("nama_wisata")
    val namaWisata: String,
    @SerializedName("rate_value")
    val rateValue: Int?
)