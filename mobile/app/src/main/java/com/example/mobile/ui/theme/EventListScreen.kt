package com.example.mobile.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.shared.EventSummary
import com.example.shared.MobileApi

sealed interface EventsUiState {
  data object Loading : EventsUiState
  data class Success(val events: List<EventSummary>) : EventsUiState
  data class Error(val message: String) : EventsUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
  api: MobileApi,
  onEventClick: (Long) -> Unit,
  onLogout: () -> Unit
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
    topBar = {
      TopAppBar(
        title = { Text("Eventos") },
        actions = {
          TextButton(onClick = onLogout) { Text("Salir") }
        }
      )
    }
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
          Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Error: ${state.message}",
              color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = { uiState = EventsUiState.Loading }) {
              Text("Reintentar")
            }
          }
        }

        is EventsUiState.Success -> {
          if (state.events.isEmpty()) {
            Text("No hay eventos", modifier = Modifier.align(Alignment.Center))
          } else {
            LazyColumn(
              modifier = Modifier.fillMaxSize(),
              contentPadding = PaddingValues(vertical = 8.dp)
            ) {
              items(state.events) { event ->
                EventItem(
                  event = event,
                  onClick = { onEventClick(event.id) }
                )
                Divider()
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
  val context = LocalContext.current

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(16.dp)
  ) {

    // ✅ IMAGEN
    AsyncImage(
      model = ImageRequest.Builder(context)
        .data(event.imageUrl)
        .crossfade(true)
        .build(),
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(88.dp)
    )

    Spacer(Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = event.title,
        style = MaterialTheme.typography.titleMedium
      )

      Spacer(Modifier.height(6.dp))

      Text(
        text = event.summary,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(Modifier.height(6.dp))

      Text(
        text = "Precio: ${event.price}",
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}
