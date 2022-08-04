package com.thechance.season2week10.models


import com.google.gson.annotations.SerializedName


data class Alerts(

    @SerializedName("alert") var alert: ArrayList<String> = arrayListOf()

)