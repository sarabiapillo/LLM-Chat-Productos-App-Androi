package com.tuapp.chatllm.util

object QrScannerUtil {
    /**
     * Extrae el slug de la empresa a partir de la URL escaneada en el código QR.
     * Ejemplo de entrada: "http://servidor/q/tech-corp"
     * Salida esperada: "tech-corp"
     */
    fun extractSlugFromUrl(url: String): String? {
        return try {
            val parts = url.split("/")
            val qIndex = parts.indexOf("q")
            if (qIndex != -1 && qIndex < parts.size - 1) {
                parts[qIndex + 1]
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
