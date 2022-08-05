package com.thechance.season2week10

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.thechance.season2week10.databinding.ActivityMainBinding


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