package com.tuapp.chatllm.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tuapp.chatllm.data.NetworkResult
import com.tuapp.chatllm.network.LoginRequest
import com.tuapp.chatllm.viewmodel.AuthViewModel

@Composable
fun ApiLoginScreen(
    viewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(LoginRequest(email, password)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is NetworkResult.Loading
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (loginState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Success -> {
                val token = (loginState as NetworkResult.Success).data.accessToken
                LaunchedEffect(Unit) {
                    onLoginSuccess(token)
                }
            }
            is NetworkResult.Error -> {
                Text(
                    text = (loginState as NetworkResult.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            null -> {}
        }
    }
}
