package com.spyneai.shootlibrary.base.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class
BaseApiClient<Api>(val BASE_URL: String, api: Class<Api>){

    var builder: Api

    fun getClient():Api{
        return builder
    }

    init  {
         builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().also { client ->
//                if (BuildConfig.DEBUG) {
//                    val logging = HttpLoggingInterceptor()
//                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//                    client.addInterceptor(logging)
//                }
            }
                .addInterceptor(RequestInterceptor())
               .addInterceptor(com.spyneai.shootlibrary.base.network.ResponseInterceptor())
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

}