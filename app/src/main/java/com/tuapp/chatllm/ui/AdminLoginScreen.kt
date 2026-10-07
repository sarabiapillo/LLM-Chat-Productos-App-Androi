package com.tuapp.chatllm.ui

import androidx.compose.runtime.Composable

@Composable
fun AdminLoginScreen(
    onLoginSuccess: () -> Unit
) {
    AuthScreen(
        onAuthSuccess = { onLoginSuccess() }
    )
}
