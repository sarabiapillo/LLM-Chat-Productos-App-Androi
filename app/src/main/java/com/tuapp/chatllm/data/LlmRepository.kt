package com.tuapp.chatllm.data

import com.tuapp.chatllm.network.ApiClient
import com.tuapp.chatllm.network.ChatCustomerRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LlmRepository {
    private val api = ApiClient.apiService

    suspend fun generateResponse(
        prompt: String,
        currentUser: UserAccount?
    ): Pair<String, List<com.tuapp.chatllm.network.RecommendedProduct>?> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Preparar el token (si el usuario está logueado)
                val token = ApiClient.getAuthToken()?.let { "Bearer $it" }

                // 2. Preparar el request para n8n
                val request = ChatCustomerRequest(
                    question = prompt,
                    clienteNombre = currentUser?.email?.substringBefore("@") ?: "Invitado"
                )

                // 3. Ejecutar la llamada a la IA de Laravel/N8N
                val response = api.askAssistant(token, request)

                if (response.isSuccessful && response.body() != null) {
                    val chatResponse = response.body()!!
                    
                    if (chatResponse.success) {
                        return@withContext Pair(chatResponse.answer, chatResponse.recommendedProducts)
                    } else {
                        return@withContext Pair("Lo siento, la IA no pudo procesar tu solicitud: ${chatResponse.status}", null)
                    }
                } else if (response.code() == 403) {
                    return@withContext Pair("Acceso denegado: Eres administrador y este canal es solo para clientes. Por favor usa la plataforma web.", null)
                } else {
                    return@withContext Pair("Error en la conexión con la IA (Código ${response.code()})", null)
                }

            } catch (e: Exception) {
                Pair("Ocurrió un error al contactar al asistente virtual: ${e.message}", null)
            }
        }
    }
}
