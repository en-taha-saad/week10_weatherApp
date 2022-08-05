package com.thechance.season2week10.models

import com.google.gson.annotations.SerializedName

data class Forecastday(

    @SerializedName("day") var day: Day? = Day(),
    @SerializedName("hour") var hour: ArrayList<Hour> = arrayListOf()

)