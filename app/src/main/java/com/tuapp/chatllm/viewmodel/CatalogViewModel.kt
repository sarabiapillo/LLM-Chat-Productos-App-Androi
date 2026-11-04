package com.tuapp.chatllm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.chatllm.data.CatalogRepository
import com.tuapp.chatllm.data.NetworkResult
import com.tuapp.chatllm.network.CatalogResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatalogViewModel : ViewModel() {
    private val repository = CatalogRepository()

    private val _catalogState = MutableStateFlow<NetworkResult<CatalogResponse>?>(null)
    val catalogState: StateFlow<NetworkResult<CatalogResponse>?> = _catalogState.asStateFlow()

    fun loadCatalog(slug: String) {
        viewModelScope.launch {
            _catalogState.value = NetworkResult.Loading
            _catalogState.value = repository.getCatalog(slug)
        }
    }
}
