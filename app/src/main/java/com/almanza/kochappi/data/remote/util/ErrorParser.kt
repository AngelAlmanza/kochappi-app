package com.almanza.kochappi.data.remote.util

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

object ErrorParser {

    fun parseError(exception: Exception): String {
        return when (exception) {
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()?.string()
                Log.e("ErrorParser", "HTTP ${exception.code()}: $errorBody")

                if (errorBody != null) {
                    try {
                        val json = Json { ignoreUnknownKeys = true }
                        // Intenta parsear como { error: "mensaje" }
                        val errorJson = json.parseToJsonElement(errorBody).jsonObject
                        errorJson["error"]?.jsonPrimitive?.content
                            ?: errorJson["message"]?.jsonPrimitive?.content
                            ?: "Error: ${exception.code()} ${exception.message()}"
                    } catch (e: Exception) {
                        Log.e("ErrorParser", "Failed to parse error body: ${e.message}")
                        "Error: ${exception.code()} ${exception.message()}"
                    }
                } else {
                    "Error: ${exception.code()} ${exception.message()}"
                }
            }
            is java.net.ConnectException -> "Error de conexión. Verifica que el servidor esté disponible."
            is java.net.SocketTimeoutException -> "Tiempo de espera agotado. Intenta de nuevo."
            else -> exception.message ?: "Error desconocido"
        }
    }
}
