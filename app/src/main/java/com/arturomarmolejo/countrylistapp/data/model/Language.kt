package com.arturomarmolejo.countrylistapp.data.model


import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("iso639_2")
    val iso6392: String? = null,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("nativeName")
    val nativeName: String? = null
)