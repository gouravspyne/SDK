package com.spyneai.shootlibrary.base.network

import com.posthog.android.Properties
import com.spyneai.shootlibrary.BaseApplication
import com.spyneai.shootlibrary.posthog.Events
import com.spyneai.shootlibrary.posthog.captureEvent
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONException
import org.json.JSONObject


class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)

        if (response.body != null){
            val body = response.body
            val bodyString = body!!.string()
            val contentType: MediaType? = body!!.contentType()

            try {

                val json = JSONObject(bodyString)
                // Log.d("ResponseInterceptor", "intercept: " + json)

                 val finalResponse = response.newBuilder().body(bodyString.toResponseBody(contentType)).build()

                if (json.has("status") && json.getInt("status") != 200) {
                    throw ServerException(json.getInt("status"),json.getString("message"))
                }

                return finalResponse
            }catch (e : JSONException){
                val properties = Properties()
                    .apply {
                        put("api",response.request.url)
                        put("response",bodyString)
                    }
                BaseApplication.getContext()
                    .captureEvent(Events.JSON_RESPONSE,properties)
            }

            return response.newBuilder().body(bodyString.toResponseBody(contentType)).build()
        }


        return response
    }
}