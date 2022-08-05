package com.thechance.season2week10.models

import com.google.gson.annotations.SerializedName


data class Day(
    @SerializedName("avgtemp_c") var avgtempC: Double? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),

)