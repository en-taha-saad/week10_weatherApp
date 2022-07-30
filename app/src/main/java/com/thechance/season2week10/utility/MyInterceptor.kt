package com.thechance.season2week10.utility

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val mRequest = chain.request()
        // to make the header sharable
//        mRequest.newBuilder().header("X-API-KEY", "1234567890").build()
        val mResponse = chain.proceed(mRequest)
        Log.i("MainActivity", "intercept: ${mResponse.code}")
        return mResponse
    }
}