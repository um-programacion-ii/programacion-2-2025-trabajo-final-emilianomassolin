package com.example.mobile.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shared.MobileApi
import com.example.shared.Seat
import com.example.shared.SeatMap
import com.example.shared.SeatState
import kotlinx.coroutines.launch

sealed interface SeatMapUiState {
  data object Loading : SeatMapUiState
  data class Success(val map: SeatMap) : SeatMapUiState
  data class Error(val message: String) : SeatMapUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatMapScreen(
  api: MobileApi,
  eventId: Long,
  refreshKey: Int,
  onBack: () -> Unit,
  onContinue: (Long, List<Pair<Int, Int>>, String?) -> Unit

) {
  var uiState by remember { mutableStateOf<SeatMapUiState>(SeatMapUiState.Loading) }
  var selected by remember { mutableStateOf<Set<Pair<Int, Int>>>(emptySet()) }
  var isBlocking by remember { mutableStateOf(false) }
  var blockError by remember { mutableStateOf<String?>(null) }

  val scope = rememberCoroutineScope()

  LaunchedEffect(eventId, refreshKey) {
    uiState = SeatMapUiState.Loading
    try {
      val map = api.getEventSeats(eventId)
      uiState = SeatMapUiState.Success(map)
    } catch (e: Exception) {
      uiState = SeatMapUiState.Error(e.message ?: "Error cargando asientos")
    }
  }


  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Mapa de asientos") },
        navigationIcon = {
          TextButton(onClick = onBack) { Text("< Volver") }
        }
      )
    }
  ) { padding ->

    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(12.dp)
    ) {

      when (val state = uiState) {

        is SeatMapUiState.Loading -> {
          CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        is SeatMapUiState.Error -> {
          Text(
            text = "Error: ${state.message}",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.align(Alignment.Center)
          )
        }

        is SeatMapUiState.Success -> {
          val map = state.map
          val allSeats = buildAllSeats(map)

          Column(Modifier.fillMaxSize()) {

            Text("Seleccionados: ${selected.size} / 4")
            Spacer(Modifier.height(8.dp))

            blockError?.let {
              Text(it, color = MaterialTheme.colorScheme.error)
              Spacer(Modifier.height(8.dp))
            }

            // 👇 CLAVE: el grid ocupa el espacio disponible y scrollea
            LazyVerticalGrid(
              columns = GridCells.Fixed(map.cols),
              verticalArrangement = Arrangement.spacedBy(6.dp),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier
                .weight(1f)        // ⭐ FIX
                .fillMaxWidth()
            ) {
              items(allSeats) { seat ->
                val key = seat.row to seat.col
                val isSelected = selected.contains(key)

                SeatCell(
                  seat = seat,
                  selected = isSelected,
                  onClick = {
                    if (seat.state == SeatState.LIBRE) {
                      selected = toggleSelection(selected, key)
                    }
                  }
                )
              }
            }

            Spacer(Modifier.height(12.dp))

            Button(
              onClick = {
                if (selected.isEmpty()) {
                  blockError = "Seleccioná al menos 1 asiento"
                  return@Button
                }

                blockError = null
                isBlocking = true

                scope.launch {
                  try {
                    val list = selected.toList()

                    api.blockSeats(eventId, list)

// ✅ como el POST no trae expiracion, la sacamos del GET
                    val selection = api.getCurrentSelection(eventId)
                    val expiresAt = selection?.expiracion

                    isBlocking = false
                    onContinue(eventId, list, expiresAt)


                  } catch (e: Exception) {
                    isBlocking = false
                    blockError = e.message ?: "Error al bloquear asientos"
                  }
                }
              },
              enabled = !isBlocking,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(if (isBlocking) "Bloqueando..." else "Bloquear y continuar")
            }
          }
        }
      }
    }
  }
}

/* ====================== Helpers ====================== */

private fun toggleSelection(
  current: Set<Pair<Int, Int>>,
  key: Pair<Int, Int>
): Set<Pair<Int, Int>> {
  return if (current.contains(key)) {
    current - key
  } else {
    if (current.size >= 4) current else current + key
  }
}

private fun buildAllSeats(map: SeatMap): List<Seat> {
  val byPos = map.seats.associateBy { it.row to it.col }
  val result = ArrayList<Seat>(map.rows * map.cols)

  for (r in 1..map.rows) {
    for (c in 1..map.cols) {
      val seat = byPos[r to c] ?: Seat(r, c, SeatState.DESCONOCIDO)
      result.add(seat)
    }
  }
  return result
}

@Composable
private fun SeatCell(
  seat: Seat,
  selected: Boolean,
  onClick: () -> Unit
) {
  val bg = when {
    selected -> MaterialTheme.colorScheme.primary
    seat.state == SeatState.LIBRE -> MaterialTheme.colorScheme.tertiary
    seat.state == SeatState.PENDIENTE -> MaterialTheme.colorScheme.secondary
    seat.state == SeatState.OCUPADO -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.surfaceVariant
  }

  Box(
    modifier = Modifier
      .size(30.dp)
      .background(bg, shape = MaterialTheme.shapes.small)
      .clickable(enabled = seat.state == SeatState.LIBRE) { onClick() },
    contentAlignment = Alignment.Center
  ) {
    val label = when (seat.state) {
      SeatState.LIBRE -> "L"
      SeatState.OCUPADO -> "O"
      SeatState.PENDIENTE -> "P"
      SeatState.DESCONOCIDO -> "?"
      SeatState.BLOQUEADO -> "B"
    }

    Text(text = label, style = MaterialTheme.typography.labelSmall)
  }
}
