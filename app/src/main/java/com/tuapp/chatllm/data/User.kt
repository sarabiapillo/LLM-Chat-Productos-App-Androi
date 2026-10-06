package com.tuapp.chatllm.data

enum class UserRole {
    CLIENT,
    ADMIN
}

data class UserAccount(
    val email: String,
    val role: UserRole
)
