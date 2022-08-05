package com.thechance.season2week10.models

import com.google.gson.annotations.SerializedName


data class Current(

    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("is_day") var isDay: Double? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),
    @SerializedName("wind_mph") var windMph: Double? = null,
    @SerializedName("wind_kph") var windKph: Double? = null,
    @SerializedName("wind_degree") var windDegree: Double? = null,
    @SerializedName("wind_dir") var windDir: String? = null,

)