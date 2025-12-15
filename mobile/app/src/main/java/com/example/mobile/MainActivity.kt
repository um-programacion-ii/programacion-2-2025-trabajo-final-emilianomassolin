package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import com.example.mobile.ui.theme.EventDetailScreen
import com.example.mobile.ui.theme.EventListScreen
import com.example.mobile.ui.theme.NamesScreen
import com.example.mobile.ui.theme.SaleResultScreen
import com.example.mobile.ui.theme.Screen
import com.example.mobile.ui.theme.SeatMapScreen
import com.example.shared.MobileApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val baseUrl = "http://10.0.2.2:8081/api/mobile"

    // Token del backend JHipster (hardcodeado por ahora)
    val tokenProvider = {
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTc2ODA2NjY5MywiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzY1NDc0NjkzLCJ1c2VySWQiOjF9.JseaEBzccfssdew1BH9u-x_JBRAHKj-Z3XtFSDPZaHnpF7ssNemKW7N3azWI6cmi6LxTVTQZdydLzgxp64rUQg"
    }

    val api = MobileApi(baseUrl, tokenProvider)

    setContent {
      MaterialTheme {
        Surface {

          var currentScreen by remember { mutableStateOf<Screen>(Screen.EventList) }
          val coroutineScope = rememberCoroutineScope() // ✅ ESTE ES EL SCOPE CORRECTO
          var seatMapRefreshKey by remember { mutableStateOf(0) }

          when (val screen = currentScreen) {

            is Screen.EventList -> {
              EventListScreen(
                api = api,
                onEventClick = { eventId ->
                  currentScreen = Screen.EventDetail(eventId)
                }
              )
            }

            is Screen.EventDetail -> {
              EventDetailScreen(
                api = api,
                eventId = screen.eventId,
                onBack = { currentScreen = Screen.EventList },
                onViewSeats = { eventId ->
                  currentScreen = Screen.SeatMap(eventId)
                },
                onResumeSelection = { eventId, seats, expiresAt ->
                  currentScreen = Screen.Names(
                    eventId = eventId,
                    seats = seats,
                    expiresAt = expiresAt
                  )
                }
              )
            }

            is Screen.SeatMap -> {
              SeatMapScreen(
                api = api,
                eventId = screen.eventId,
                refreshKey = screen.refreshKey,   // 👈 ACÁ ESTABA EL ERROR
                onBack = {
                  currentScreen = Screen.EventDetail(screen.eventId)
                },
                onContinue = { eventId, seats, expiresAt ->
                  currentScreen = Screen.Names(
                    eventId = eventId,
                    seats = seats,
                    expiresAt = expiresAt
                  )
                }
              )
            }


            is Screen.Names -> {
              NamesScreen(
                eventId = screen.eventId,
                seats = screen.seats,
                expiresAt = screen.expiresAt,
                onBack = { currentScreen = Screen.SeatMap(screen.eventId) },
                onExpired = { currentScreen = Screen.EventList },
                onConfirm = { seatsWithPeople ->
                  coroutineScope.launch {
                    try {
                      // 🔹 Nos quedamos SOLO con los nombres
                      val personas = seatsWithPeople.map { it.third }

                      val resp = api.sell(
                        eventoId = screen.eventId,
                        fecha = "2025-12-11T18:00:00Z",
                        precioVenta = 1500.0,
                        personas = personas          // ✅ AHORA ES LO QUE ESPERA EL BACKEND
                      )

                      currentScreen = Screen.SaleResult(
                        success = resp.resultado == true,
                        message = resp.descripcion ?: "Sin mensaje"
                      )
                    } catch (e: Exception) {
                      currentScreen = Screen.SaleResult(
                        success = false,
                        message = e.message ?: "Error inesperado"
                      )
                    }
                  }
                }


              )
            }

            is Screen.SaleResult -> {
              SaleResultScreen(
                success = screen.success,
                message = screen.message,
                onFinish = { currentScreen = Screen.EventList }
              )
            }
          }
        }
      }
    }
  }
}
