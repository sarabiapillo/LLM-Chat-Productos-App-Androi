package com.tuapp.chatllm.data

import com.tuapp.chatllm.network.ApiClient
import com.tuapp.chatllm.network.CatalogResponse
import com.tuapp.chatllm.network.SmartFilterRequest
import com.tuapp.chatllm.network.SmartFilterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatalogRepository {
    private val api = ApiClient.apiService

    suspend fun getCatalog(slug: String): NetworkResult<CatalogResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCatalogBySlug(slug)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error("Error al cargar catálogo: ${response.message()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }

    suspend fun applySmartFilter(request: SmartFilterRequest): NetworkResult<SmartFilterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.filterProductsSmart(request)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error("Error en filtro inteligente: ${response.message()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de red: ${e.message}")
            }
        }
    }
}
