package com.example.mobile.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NamesScreen(
  eventId: Long,
  seats: List<Pair<Int, Int>>,
  expiresAt: String?,
  onBack: () -> Unit,
  onExpired: () -> Unit,
  onConfirm: (List<Triple<Int, Int, String>>) -> Unit
) {
  /* ================= Cronómetro ================= */



  var remainingSeconds by remember(expiresAt) {
    mutableStateOf<Long?>(
      expiresAt?.let { isoRaw ->
        try {
          // ✅ backend manda "....43.800069Z" (microsegundos). Lo normalizamos a "....43Z"
          val iso = isoRaw.replace(Regex("\\.\\d+Z$"), "Z")

          val sdf = java.text.SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            java.util.Locale.US
          )
          sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")

          val expiryMillis = sdf.parse(iso)?.time ?: return@let null
          val nowMillis = System.currentTimeMillis()

          ((expiryMillis - nowMillis) / 1000).coerceAtLeast(0)
        } catch (e: Exception) {
          null
        }
      }
    )
  }


  LaunchedEffect(expiresAt) {
    val start = remainingSeconds ?: return@LaunchedEffect
    var s = start
    while (s > 0) {
      kotlinx.coroutines.delay(1_000)
      s--
      remainingSeconds = s
    }
    if (s <= 0) onExpired()
  }






  /* ================= Nombres ================= */

  val names = remember(seats) {
    mutableStateListOf<String>().apply {
      repeat(seats.size) { add("") }
    }
  }

  /* ================= UI ================= */

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Nombres") },
        navigationIcon = {
          TextButton(onClick = onBack) { Text("< Volver") }
        }
      )
    }
  ) { padding ->

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
    ) {

      Text("Evento: $eventId")
      Spacer(Modifier.height(8.dp))

      when {
        remainingSeconds == null -> {
          Text("Tiempo restante: (no disponible)", color = MaterialTheme.colorScheme.primary)
        }
        remainingSeconds!! > 0 -> {
          Text("Tiempo restante: ${remainingSeconds}s", color = MaterialTheme.colorScheme.primary)
        }
        else -> {
          Text("Sesión expirada", color = MaterialTheme.colorScheme.error)
        }
      }


      Spacer(Modifier.height(16.dp))
      Text("Cargá nombre para cada asiento:")
      Spacer(Modifier.height(12.dp))

      seats.forEachIndexed { index, (r, c) ->
        OutlinedTextField(
          value = names[index],
          onValueChange = { names[index] = it },
          label = { Text("Asiento ($r,$c) - Nombre") },
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
      }

      Spacer(Modifier.height(24.dp))

      Button(
        onClick = {
          val payload = seats.mapIndexed { index, (r, c) ->
            Triple(r, c, names[index])
          }
          onConfirm(payload)
        },
        enabled = names.all { it.isNotBlank() } && (remainingSeconds == null || remainingSeconds!! > 0),

          modifier = Modifier.fillMaxWidth()
      ) {
        Text("Confirmar (siguiente: venta)")
      }
    }
  }
}
