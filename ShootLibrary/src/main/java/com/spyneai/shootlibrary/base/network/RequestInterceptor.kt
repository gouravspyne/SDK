package com.spyneai.shootlibrary.base.network

import com.spyneai.shootlibrary.getRequestHeaderData
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("device_details", getRequestHeaderData().toString())
            .build()

        val s = ""

        return chain.proceed(request)
    }
}