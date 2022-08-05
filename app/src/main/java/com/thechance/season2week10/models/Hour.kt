package com.thechance.season2week10.models

import com.google.gson.annotations.SerializedName


data class Hour(

    @SerializedName("time") var time: String? = null,
    @SerializedName("temp_c") var tempC: Double? = null,
    @SerializedName("is_day") var isDay: Double? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),

    )