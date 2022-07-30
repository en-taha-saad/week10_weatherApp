package com.thechance.season2week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.thechance.season2week10.databinding.ActivityMainBinding
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

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
        binding.button.setOnClickListener {
            makeRequestUsingOKHTTPWithQuery(binding.editText.text.toString())
        }
    }

    // just to get information from the server without giving any query or path parameter
    private fun makeRequestUsingOKHTTP() {
        Log.i("MainActivity", "makeRequestUsingOKHTTP")
        val request = Request.Builder()
            .url("https://v2.jokeapi.dev/joke/Any")
            .build()

        // will be executed in the main thread
//        val response = okHttpClient.newCall(request).execute()
//        response.body.toString()


        // will be executed on another thread
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("MainActivity", "failure ${e.message}")

            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    binding.text.text = response.body?.string().toString()
                }
                Log.i("MainActivity", "success ${response.body?.string().toString()}")

            }

        })
    }

    // to get information from the server with query parameter
    private fun makeRequestUsingOKHTTPWithQuery(name: String) {
        Log.i("MainActivity", "makeRequestUsingOKHTTP")
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.nationalize.io")
            .addQueryParameter("name", name)
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
//                    val json = JSONObject(jsonString.toString()).toNationalResponse()
                    val json = Gson().fromJson(jsonString, NationalResponse::class.java)
                    Log.i("MainActivity", "success $json")
                    runOnUiThread {
                        binding.text.text = json.countries[1].countryID
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