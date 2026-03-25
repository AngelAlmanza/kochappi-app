package com.almanza.kochappi.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Log request
        Log.d(TAG, "═══════════════════════════════════════")
        Log.d(TAG, "🔵 REQUEST")
        Log.d(TAG, "═══════════════════════════════════════")
        Log.d(TAG, "URL: ${request.url}")
        Log.d(TAG, "Method: ${request.method}")
        Log.d(TAG, "Headers: ${request.headers}")

        if (request.body != null) {
            val buffer = Buffer()
            request.body!!.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            Log.d(TAG, "Body: $bodyString")
        }

        // Execute request
        val startTime = System.currentTimeMillis()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "❌ REQUEST FAILED")
            Log.e(TAG, "Error: ${e.message}")
            Log.e(TAG, "StackTrace: ${e.stackTraceToString()}")
            throw e
        }
        val duration = System.currentTimeMillis() - startTime

        // Log response
        Log.d(TAG, "═══════════════════════════════════════")
        Log.d(TAG, "🟢 RESPONSE (${duration}ms)")
        Log.d(TAG, "═══════════════════════════════════════")
        Log.d(TAG, "Code: ${response.code}")
        Log.d(TAG, "Message: ${response.message}")
        Log.d(TAG, "Headers: ${response.headers}")

        // Read response body
        val responseBody = response.body
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer
            val bodyString = buffer.clone().readUtf8()

            when {
                response.code == 200 || response.code == 201 -> {
                    Log.d(TAG, "✅ Success Body: $bodyString")
                }
                response.code == 400 -> {
                    Log.w(TAG, "⚠️ Bad Request Body: $bodyString")
                }
                response.code == 401 -> {
                    Log.w(TAG, "⚠️ Unauthorized Body: $bodyString")
                }
                response.code >= 500 -> {
                    Log.e(TAG, "❌ Server Error Body: $bodyString")
                }
                else -> {
                    Log.w(TAG, "⚠️ Error Body: $bodyString")
                }
            }

            // Return response with body (since we consumed it)
            return response.newBuilder()
                .body(okhttp3.ResponseBody.create(responseBody.contentType(), bodyString.toByteArray()))
                .build()
        }

        Log.d(TAG, "═══════════════════════════════════════")

        return response
    }

    companion object {
        private const val TAG = "KochappiAPI"
    }
}
