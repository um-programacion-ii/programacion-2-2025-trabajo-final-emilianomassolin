package com.example.mobile.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.EventDetail
import com.example.shared.MobileApi

sealed interface EventDetailUiState {
  object Loading : EventDetailUiState
  data class Success(val event: EventDetail) : EventDetailUiState
  data class Error(val message: String) : EventDetailUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
  api: MobileApi,
  eventId: Long,
  onBack: () -> Unit
) {
  var uiState by remember { mutableStateOf<EventDetailUiState>(EventDetailUiState.Loading) }

  LaunchedEffect(eventId) {
    uiState = EventDetailUiState.Loading
    try {
      val event = api.getEventDetail(eventId)
      uiState = EventDetailUiState.Success(event)
    } catch (e: Exception) {
      uiState = EventDetailUiState.Error(e.message ?: "Error desconocido")
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Detalle") },
        navigationIcon = {
          TextButton(onClick = onBack) {
            Text("< Volver")
          }
        }
      )
    }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
    ) {
      when (val state = uiState) {
        is EventDetailUiState.Loading -> {
          CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        is EventDetailUiState.Error -> {
          Text(
            text = "Error: ${state.message}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.align(Alignment.Center)
          )
        }
        is EventDetailUiState.Success -> {
          val event = state.event
          Column(
            modifier = Modifier.fillMaxSize()
          ) {
            Text(event.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text(event.description)
            Spacer(Modifier.height(16.dp))
            Text("Precio: ${event.price}")
            Spacer(Modifier.height(8.dp))
            Text("Asientos: ${event.rows} filas x ${event.columns} columnas")
            Spacer(Modifier.height(8.dp))
            event.integrantes?.let {
              Text("Integrantes: $it")
            }
          }
        }
      }
    }
  }
}
