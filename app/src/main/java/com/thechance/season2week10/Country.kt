package com.thechance.season2week10

import com.google.gson.annotations.SerializedName


data class Country(
    @SerializedName("country_id") val countryID: String,
    val probability: String
)
