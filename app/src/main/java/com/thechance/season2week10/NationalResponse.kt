package com.thechance.season2week10

import com.google.gson.annotations.SerializedName

data class NationalResponse(
    val name: String,
    @SerializedName("country") val countries: List<Country>
)

