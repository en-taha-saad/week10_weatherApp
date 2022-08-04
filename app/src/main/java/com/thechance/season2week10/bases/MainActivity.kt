package com.thechance.season2week10

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import okhttp3.logging.HttpLoggingInterceptor
import com.thechance.season2week10.databinding.ActivityMainBinding
import com.thechance.season2week10.models.WeatherAPI



class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val homeFragment = HomeFragment()

    override val logTag = MainActivity::class.simpleName
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate

    override fun setup() {
        replaceFragment(homeFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.sub_view_container, fragment)
        transaction.commit()
    }

}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val logInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(logInterceptor).build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentWeatherInformation()

    }

    private fun getCurrentWeatherInformation() {
        Log.i("MainActivity", "getCurrentWeatherInformation")
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.weatherbit.io")
            .addPathSegment("v2.0")
            .addPathSegment("current")
            .addQueryParameter("lat", "37.8267")
            .addQueryParameter("lon", "122.4233")
            .addQueryParameter("key", "7e55a1c2b46f4f60b1cf9fcb7e597261")
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
                    runOnUiThread {
//                        binding.cityTextView.text = json.data[0].cityName
//                        binding.countryTextView.text =
//                            "${json.data[0].countryCode} ${json.data[0].stateCode}"
                    }
                }
            }
        })
    }
}

/*
Okhttp Interceptor
 there is something important, we need to consider while building the offline-first app.
The cached response will be returned only when the Internet is available as OkHttp is designed like that.
When the Internet is available and data is cached, it returns the data from the cache.
Even when the data is cached and the Internet is not available, it returns with the error "no internet available".

What to do now?
We can use the following ForceCacheInterceptor at the Application layer in addition to the above one (CacheInterceptor, only if not enabled from the server). To implement in the code, we will create a ForceCacheInterceptor like below:

class ForceCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!IsInternetAvailable()) {
            builder.cacheControl(CacheControl.FORCE_CACHE);

        }
        return chain.proceed(builder.build());
    }
}
We can add the Interceptor in OkHttpClient like below:
.addNetworkInterceptor(CacheInterceptor()) // only if not enabled from the server
.addInterceptor(ForceCacheInterceptor())
Here, we are adding the ForceCacheInterceptor to OkHttpClient using addInterceptor() and not addNetworkInterceptor() as we want it to work on the Application layer.


max-age vs max-stale
max-age is the oldest limit ( lower limit) till which the response can be returned from the cache.
max-stale is the highest limit beyond which cache cannot be returned.
*/