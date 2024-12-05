package com.retofit.app.network.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MyAppInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val headers = request.headers.newBuilder()
            .build()

        request = request.newBuilder().headers(headers).build()

        return chain.proceed(request)
    }

}