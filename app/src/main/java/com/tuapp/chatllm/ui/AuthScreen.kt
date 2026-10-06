package com.tuapp.chatllm.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tuapp.chatllm.R
import com.tuapp.chatllm.data.ContextRepository
import com.tuapp.chatllm.data.UserAccount
import com.tuapp.chatllm.data.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    repository: ContextRepository,
    onAuthSuccess: (UserAccount) -> Unit
) {
    val context = LocalContext.current
    var isRegisterMode by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isAwaiting2FA by remember { mutableStateOf(false) }
    var generated2FACode by remember { mutableStateOf("123456") }
    var entered2FACode by remember { mutableStateOf("123456") }
    var showEmailSimulationDialog by remember { mutableStateOf(false) }

    var pendingEmail by remember { mutableStateOf("") }
    var pendingPassword by remember { mutableStateOf("") }
    var pendingRole by remember { mutableStateOf(UserRole.CLIENT) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Si concede el permiso, la próxima vez le llegará arriba. Por ahora mostramos el diálogo.
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun sendSecurityNotificationEmail(targetEmail: String, code: String) {
        showEmailSimulationDialog = true
        SecurityNotificationHelper.send2FAEmailNotification(context, targetEmail, code)
    }

    if (showEmailSimulationDialog) {
        AlertDialog(
            onDismissRequest = { showEmailSimulationDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "📩 Correo Electrónico Recibido",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "De: seguridad@chatllm.com",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Para: $pendingEmail",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colorResource(R.color.mint_light_container),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Asunto: Código de Verificación 2FA",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Hola, tu código de verificación de 6 dígitos para completar tu acceso es:",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = generated2FACode,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showEmailSimulationDialog = false
                }) {
                    Text("Entendido")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailSimulationDialog = false }) {
                    Text("Cerrar Correo")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isAwaiting2FA) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.auth_2fa_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.auth_2fa_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = colorResource(R.color.mint_light_container),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "✅ Código 2FA Listo para Activar:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.active_green)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Correo asignado: $pendingEmail",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Código cargado automáticamente: $generated2FACode",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = entered2FACode,
                        onValueChange = {
                            entered2FACode = it.take(6)
                            errorMessage = null
                        },
                        label = { Text(stringResource(R.string.auth_2fa_code_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (entered2FACode.length == 6) {
                                if (isRegisterMode) {
                                    val newAccount = repository.registerUser(pendingEmail, pendingPassword, UserRole.CLIENT)
                                    if (newAccount != null) {
                                        onAuthSuccess(newAccount)
                                    } else {
                                        val existing = repository.loginUser(pendingEmail, pendingPassword)
                                        if (existing != null) {
                                            onAuthSuccess(existing)
                                        } else {
                                            errorMessage = "Error al completar registro."
                                        }
                                    }
                                } else {
                                    val loggedIn = repository.loginUser(pendingEmail, pendingPassword)
                                    if (loggedIn != null) {
                                        onAuthSuccess(loggedIn)
                                    } else {
                                        errorMessage = "Error al iniciar sesión."
                                    }
                                }
                            } else {
                                errorMessage = "Ingresa un código de 6 dígitos"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = entered2FACode.length == 6
                    ) {
                        Text(
                            text = "Confirmar y Activar Cuenta",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = {
                            showEmailSimulationDialog = true
                        }) {
                            Text("📩 Ver Correo Recibido")
                        }

                        TextButton(onClick = {
                            generated2FACode = "123456"
                            entered2FACode = "123456"
                            errorMessage = "Código reenviado y cargado."
                            sendSecurityNotificationEmail(pendingEmail, generated2FACode)
                        }) {
                            Text(stringResource(R.string.auth_2fa_resend))
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TabRow(
                        selectedTabIndex = if (isRegisterMode) 1 else 0,
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = !isRegisterMode,
                            onClick = {
                                isRegisterMode = false
                                errorMessage = null
                            },
                            text = { Text(stringResource(R.string.auth_login_tab), fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = isRegisterMode,
                            onClick = {
                                isRegisterMode = true
                                errorMessage = null
                            },
                            text = { Text(stringResource(R.string.auth_register_tab), fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = { Text(stringResource(R.string.auth_email_label)) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text(stringResource(R.string.auth_password_label)) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isRegisterMode) {
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                errorMessage = null
                            },
                            label = { Text(stringResource(R.string.auth_confirm_password_label)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (isRegisterMode) {
                                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                    errorMessage = "Por favor completa todos los campos"
                                } else if (password != confirmPassword) {
                                    errorMessage = "Las contraseñas no coinciden"
                                } else if (repository.isEmailRegistered(email)) {
                                    errorMessage = "Este correo electrónico ya se encuentra registrado. Por favor inicia sesión."
                                } else {
                                    pendingEmail = email.trim()
                                    pendingPassword = password.trim()
                                    pendingRole = UserRole.CLIENT

                                    generated2FACode = "123456"
                                    entered2FACode = "123456"
                                    isAwaiting2FA = true

                                    sendSecurityNotificationEmail(pendingEmail, generated2FACode)
                                }
                            } else {
                                if (email.isBlank() || password.isBlank()) {
                                    errorMessage = "Ingresa correo y contraseña"
                                } else {
                                    val user = repository.loginUser(email, password)
                                    if (user != null) {
                                        pendingEmail = user.email
                                        pendingPassword = password.trim()
                                        pendingRole = user.role

                                        generated2FACode = "123456"
                                        entered2FACode = "123456"
                                        isAwaiting2FA = true

                                        sendSecurityNotificationEmail(pendingEmail, generated2FACode)
                                    } else {
                                        errorMessage = "Correo o contraseña incorrectos"
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isRegisterMode)
                                stringResource(R.string.auth_register_btn)
                            else
                                stringResource(R.string.auth_login_btn),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    MaterialTheme {
        AuthScreen(
            repository = ContextRepository(),
            onAuthSuccess = {}
        )
    }
}
