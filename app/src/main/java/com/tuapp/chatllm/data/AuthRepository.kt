package com.tuapp.chatllm.data

import com.tuapp.chatllm.network.ApiClient
import com.tuapp.chatllm.network.LoginRequest
import com.tuapp.chatllm.network.LoginResponse
import com.tuapp.chatllm.network.RegisterRequest
import com.tuapp.chatllm.network.RegisterResponse
import com.tuapp.chatllm.network.Resend2faRequest
import com.tuapp.chatllm.network.Verify2faRequest
import com.tuapp.chatllm.network.Verify2faResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val api = ApiClient.apiService

    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(request)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    ApiClient.setAuthToken(loginResponse.accessToken)
                    NetworkResult.Success(loginResponse)
                } else {
                    NetworkResult.Error("Error de credenciales: ${response.message()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun register(request: RegisterRequest): NetworkResult<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(request)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error("Error al registrar: ${response.code()} ${response.errorBody()?.string()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun verify2fa(request: Verify2faRequest): NetworkResult<Verify2faResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.verify2fa(request)
                if (response.isSuccessful && response.body() != null) {
                    NetworkResult.Success(response.body()!!)
                } else {
                    NetworkResult.Error("Código inválido: ${response.message()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun resend2fa(request: Resend2faRequest): NetworkResult<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.resend2fa(request)
                if (response.isSuccessful) {
                    NetworkResult.Success("Código reenviado")
                } else {
                    NetworkResult.Error("Error al reenviar: ${response.message()}", response.code())
                }
            } catch (e: Exception) {
                NetworkResult.Error("Error de conexión: ${e.message}")
            }
        }
    }

    suspend fun logout(): NetworkResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.logout()
                ApiClient.setAuthToken(null)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                ApiClient.setAuthToken(null) // Limpiar de todas formas
                NetworkResult.Error("Error al cerrar sesión")
            }
        }
    }
}
