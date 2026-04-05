package com.example.mobile.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.AuthApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
  authApi: AuthApi,
  onLoginSuccess: (String) -> Unit
) {
  var user by remember { mutableStateOf("admin") }
  var pass by remember { mutableStateOf("admin") }
  var loading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }
  val scope = rememberCoroutineScope()

  Scaffold(
    topBar = { TopAppBar(title = { Text("Login") }) }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
    ) {
      OutlinedTextField(
        value = user,
        onValueChange = { user = it },
        label = { Text("Usuario") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(Modifier.height(12.dp))
      OutlinedTextField(
        value = pass,
        onValueChange = { pass = it },
        label = { Text("Contraseña") },
        modifier = Modifier.fillMaxWidth()
      )
      Spacer(Modifier.height(16.dp))

      error?.let {
        Text(it, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(8.dp))
      }

      Button(
        enabled = !loading && user.isNotBlank() && pass.isNotBlank(),
        modifier = Modifier.fillMaxWidth(),
        onClick = {
          loading = true
          error = null
          scope.launch {
            try {
              val token = authApi.login(user, pass)
              loading = false
              onLoginSuccess(token)
            } catch (e: Exception) {
              loading = false
              error = e.message ?: "Error de login"
            }
          }
        }
      ) {
        Text(if (loading) "Ingresando..." else "Ingresar")
      }
    }
  }
}
