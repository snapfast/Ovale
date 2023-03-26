package bali.rahul.ovale.auth

import bali.rahul.ovale.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


public class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var aToken = BuildConfig.ACCESS_TOKEN

        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Client-ID $aToken")
        return chain.proceed(request.build())
    }

}
