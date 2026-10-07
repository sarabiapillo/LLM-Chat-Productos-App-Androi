package com.tuapp.chatllm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuapp.chatllm.data.AuthRepository
import com.tuapp.chatllm.data.NetworkResult
import com.tuapp.chatllm.network.LoginRequest
import com.tuapp.chatllm.network.LoginResponse
import com.tuapp.chatllm.network.RegisterRequest
import com.tuapp.chatllm.network.RegisterResponse
import com.tuapp.chatllm.network.Resend2faRequest
import com.tuapp.chatllm.network.Verify2faRequest
import com.tuapp.chatllm.network.Verify2faResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<NetworkResult<LoginResponse>?>(null)
    val loginState: StateFlow<NetworkResult<LoginResponse>?> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<NetworkResult<RegisterResponse>?>(null)
    val registerState: StateFlow<NetworkResult<RegisterResponse>?> = _registerState.asStateFlow()

    private val _verify2faState = MutableStateFlow<NetworkResult<Verify2faResponse>?>(null)
    val verify2faState: StateFlow<NetworkResult<Verify2faResponse>?> = _verify2faState.asStateFlow()

    private val _resend2faState = MutableStateFlow<NetworkResult<String>?>(null)
    val resend2faState: StateFlow<NetworkResult<String>?> = _resend2faState.asStateFlow()

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading
            _loginState.value = repository.login(request)
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _registerState.value = NetworkResult.Loading
            _registerState.value = repository.register(request)
        }
    }

    fun verify2fa(request: Verify2faRequest) {
        viewModelScope.launch {
            _verify2faState.value = NetworkResult.Loading
            _verify2faState.value = repository.verify2fa(request)
        }
    }

    fun resend2fa(request: Resend2faRequest) {
        viewModelScope.launch {
            _resend2faState.value = NetworkResult.Loading
            _resend2faState.value = repository.resend2fa(request)
        }
    }

    fun resetAuthStates() {
        _loginState.value = null
        _registerState.value = null
        _verify2faState.value = null
        _resend2faState.value = null
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            resetAuthStates()
        }
    }
}
