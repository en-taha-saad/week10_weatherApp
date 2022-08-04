package com.thechance.season2week10.models

import com.google.gson.annotations.SerializedName

data class WeatherAPI(
    @SerializedName("location") val location: Location?,
    @SerializedName("current") val current: Current?,
    @SerializedName("forecast") val forecast: Forecast?,
    @SerializedName("alerts") val alerts: Alerts?,

    )