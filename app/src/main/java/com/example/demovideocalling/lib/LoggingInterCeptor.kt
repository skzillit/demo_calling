package com.example.demovideocalling.lib

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log request headers
        println("🔹 Request URL: ${request.url}")
        println("🔹 Request Headers:")
        request.headers.forEach { header ->
            println("   ${header.first}: ${header.second}")
        }

        return chain.proceed(request)
    }
}