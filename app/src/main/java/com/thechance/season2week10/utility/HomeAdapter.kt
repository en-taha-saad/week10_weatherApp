package com.thechance.season2week10.utility

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thechance.season2week10.R
import com.thechance.season2week10.databinding.WeatherRvItemBinding
import com.thechance.season2week10.models.Hour


class HomeAdapter(private val addedWeatherConditions: List<Hour>) :
    RecyclerView.Adapter<HomeAdapter.WeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_rv_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentWeatherCondition = addedWeatherConditions[position]
        holder.binding.apply {
            idIVTime.text = currentWeatherCondition.time?.split(" ")!![1]
            idIVTempreture.text = currentWeatherCondition.tempC.toString()
            Glide.with(holder.itemView.context)
                .load("https:${currentWeatherCondition.condition?.icon}")
                .into(idIVCondition)
            idTVWindSpeed.text = currentWeatherCondition.condition?.text

        }
    }

    override fun getItemCount() = addedWeatherConditions.size

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = WeatherRvItemBinding.bind(itemView)
    }
}