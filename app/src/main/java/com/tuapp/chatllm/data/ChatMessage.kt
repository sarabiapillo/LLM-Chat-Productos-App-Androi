package com.tuapp.chatllm.data

import com.tuapp.chatllm.network.RecommendedProduct

enum class MessageSender {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val recommendedProducts: List<RecommendedProduct>? = null
)
