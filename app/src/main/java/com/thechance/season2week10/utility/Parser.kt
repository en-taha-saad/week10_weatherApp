package com.thechance.season2week10.utility

import com.thechance.season2week10.Country
import com.thechance.season2week10.NationalResponse
import org.json.JSONObject

fun JSONObject.toNationalResponse(): NationalResponse {
    val name = this.getString("name")
    val countryListJson = this.getJSONArray("country")
    val countryList = mutableListOf<Country>()
    for (i in 0 until countryListJson.length()) {
        val countryJsonObject = countryListJson[i] as JSONObject
        val countryId = countryJsonObject.getString("country_id")
        val probability = countryJsonObject.getString("probability")
        countryList.add(Country(countryId, probability))
    }
    return NationalResponse(name, countryList)
}