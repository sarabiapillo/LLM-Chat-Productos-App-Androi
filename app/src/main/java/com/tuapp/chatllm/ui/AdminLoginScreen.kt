package com.tuapp.chatllm.ui

import androidx.compose.runtime.Composable
import com.tuapp.chatllm.data.ContextRepository

@Composable
fun AdminLoginScreen(
    repository: ContextRepository,
    onLoginSuccess: () -> Unit
) {
    AuthScreen(
        repository = repository,
        onAuthSuccess = { onLoginSuccess() }
    )
}
