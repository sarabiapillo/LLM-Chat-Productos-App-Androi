package com.tuapp.chatllm

import androidx.compose.runtime.Composable
import com.tuapp.chatllm.data.ContextRepository
import com.tuapp.chatllm.ui.ChatScreen as UiChatScreen

@Composable
fun ChatScreen(repository: ContextRepository) {
    UiChatScreen(
        repository = repository,
        currentUser = null,
        onNavigateToAuth = {}
    )
}
