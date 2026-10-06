package com.tuapp.chatllm.data

import kotlinx.coroutines.delay

class LlmRepository {

    suspend fun generateResponse(
        userPrompt: String,
        activeContexts: List<ContextItem>
    ): String {
        delay(1200)

        val contextSummary = if (activeContexts.isNotEmpty()) {
            activeContexts.joinToString("\n") { "• [${it.title}]: ${it.content}" }
        } else {
            "Ningún contexto de instrucción activo."
        }

        return buildString {
            append("Hola. He recibido tu consulta:\n\"")
            append(userPrompt.trim())
            append("\"\n\n")
            append("Aplicando las siguientes reglas de contexto activo:\n")
            append(contextSummary)
            append("\n\nProcesado con éxito. ¡Puedo ayudarte con más detalles sobre tu desarrollo en Android o arquitectura de software!")
        }
    }
}
