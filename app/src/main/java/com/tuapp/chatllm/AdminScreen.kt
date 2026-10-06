package com.tuapp.chatllm

import androidx.compose.runtime.Composable
import com.tuapp.chatllm.data.ContextRepository
import com.tuapp.chatllm.ui.AdminScreen as UiAdminScreen

@Composable
fun AdminScreen(repository: ContextRepository) {
    UiAdminScreen(repository = repository)
}
