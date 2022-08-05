package com.thechance.season2week10

import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.TypedArrayUtils.getString
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.thechance.season2week10.utility.HomeAdapter
import com.thechance.season2week10.databinding.FragmentHomeBinding
import com.thechance.season2week10.models.WeatherAPI
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    var weatherAPI: WeatherAPI? = null

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logInterceptor).build()

    override val logTag: String? = HomeFragment::class.java.simpleName
    override val bindingInflater: (LayoutInflater) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate


    override fun setup() {
        binding.apply {
            idPBLoading.visibility = View.VISIBLE
            idRLHome.visibility = View.GONE
            idIVSearch.setOnClickListener {
                idPBLoading.visibility = View.VISIBLE
                idRLHome.visibility = View.GONE
                idTVCityName.text = idTIECity.text.toString()
                getCurrentWeatherInformation(idTIECity.text.toString())
            }

        }
        getCurrentWeatherInformation()
    }


    private fun getCurrentWeatherInformation(city: String = "Baghdad") {
        Log.i("MainActivity", "getCurrentWeatherInformation")
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.weatherapi.com")
            .addPathSegment("v1")
            .addPathSegment("forecast.json")
            .addQueryParameter("key", "6998f8e6cd4743c8879161347211710")
            .addQueryParameter("q", city)
            .addQueryParameter("days", "1")
            .addQueryParameter("aqi", "yes")
            .addQueryParameter("alerts", "yes")
            .build()
        val request = Request.Builder()
            .url(url)
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("MainActivity", "failure ${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string().let { jsonString ->
                    val json = Gson().fromJson(jsonString, WeatherAPI::class.java)
                    Log.i("MainActivity", "success $json")

                    activity?.runOnUiThread {
                        binding.apply {
                            idPBLoading.visibility = View.GONE
                            idRLHome.visibility = View.VISIBLE
                            if (json.current != null) {
                                idTVCondition.visibility = View.VISIBLE
                                idTVTempreture.visibility = View.VISIBLE
                                idTVCityName.visibility = View.VISIBLE
                                idTVIcon.visibility = View.VISIBLE
                                notFound.visibility = View.GONE

                                binding.idRvWeather.adapter =
                                    HomeAdapter(
                                        json?.forecast?.forecastday?.get(0)?.hour ?: emptyList()
                                    )

                                Glide.with(this@HomeFragment)
                                    .load("https:${json.current.condition?.icon}")
                                    .into(idTVIcon)
                                idTVCondition.text = json.current.condition?.text
                                idTVTempreture.text = json.current.tempC.toString()
                                idTVCityName.text = json.location?.name
                            } else {
                                notFound.visibility = View.VISIBLE
                                idTVCondition.visibility = View.GONE
                                idTVTempreture.visibility = View.GONE
                                idTVIcon.visibility = View.GONE
                                binding.idRvWeather.adapter =
                                    HomeAdapter(
                                        emptyList()
                                    )
                                idTVCityName.text = idTIECity.text.toString()
                            }
                        }

                    }
                }
            }
        })
    }


}