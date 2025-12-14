package com.example.mobile.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shared.EventSummary
import com.example.shared.MobileApi

sealed interface EventsUiState {
  object Loading : EventsUiState
  data class Success(val events: List<EventSummary>) : EventsUiState
  data class Error(val message: String) : EventsUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
  api: MobileApi,
  onEventClick: (Long) -> Unit
) {
  var uiState by remember { mutableStateOf<EventsUiState>(EventsUiState.Loading) }

  LaunchedEffect(Unit) {
    uiState = EventsUiState.Loading
    try {
      val events = api.getEvents()
      uiState = EventsUiState.Success(events)
    } catch (e: Exception) {
      uiState = EventsUiState.Error(e.message ?: "Error desconocido")
    }
  }

  Scaffold(
    topBar = { TopAppBar(title = { Text("Eventos") }) }
  ) { padding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      when (val state = uiState) {
        is EventsUiState.Loading -> {
          CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        is EventsUiState.Error -> {
          Text(
            text = "Error: ${state.message}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.align(Alignment.Center)
          )
        }
        is EventsUiState.Success -> {
          if (state.events.isEmpty()) {
            Text(
              text = "No hay eventos",
              modifier = Modifier.align(Alignment.Center)
            )
          } else {
            LazyColumn {
              items(state.events) { event ->
                EventItem(
                  event = event,
                  onClick = { onEventClick(event.id) } // 👈 acá navega
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun EventItem(
  event: EventSummary,
  onClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() } // 👈 zona clickeable
      .padding(16.dp)
  ) {
    Text(event.title, style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(4.dp))
    Text(
      event.summary,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(4.dp))
    Text("Precio: ${event.price}", style = MaterialTheme.typography.bodySmall)
  }
}
