package com.indexdev.tourin.data.model.response


import com.google.gson.annotations.SerializedName

data class UserMitra(
    @SerializedName("id_mitra")
    val idMitra: String,
    @SerializedName("nama_pemilik")
    val namaPemilik: String,
    @SerializedName("nama_usaha")
    val namaUsaha: String,
    @SerializedName("email_pemilik")
    val emailPemilik: String,
    @SerializedName("no_ponsel")
    val noPonsel: String,
    @SerializedName("jenis_usaha")
    val jenisUsaha: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("alamat")
    val alamat: Any?,
    @SerializedName("hari_buka")
    val hariBuka: Any?,
    @SerializedName("jam_buka")
    val jamBuka: Any?,
    @SerializedName("jam_tutup")
    val jamTutup: Any?,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("longi")
    val longi: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("status")
    val status: String
)