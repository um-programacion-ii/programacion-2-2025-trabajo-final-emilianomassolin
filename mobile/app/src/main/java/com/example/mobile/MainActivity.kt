package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import com.example.mobile.ui.theme.*
import com.example.shared.AuthApi
import com.example.shared.MobileApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val backendUrl = "http://10.0.2.2:8081"
    val mobileBaseUrl = "$backendUrl/api/mobile"

    val authApi = AuthApi(backendUrl)

    setContent {
      MaterialTheme {
        Surface {

          val coroutineScope = rememberCoroutineScope()

          // ✅ token en memoria (simple). Si querés persistencia, después lo pasamos a SharedPreferences.
          var token by remember { mutableStateOf<String?>(null) }

          // navegación simple
          var currentScreen by remember { mutableStateOf<Screen>(Screen.EventList) }

          // refresh del mapa
          var seatMapRefreshKey by remember { mutableStateOf(0) }

          // ✅ MobileApi siempre usa el token actual
          val api = remember(token) {
            MobileApi(
              baseUrl = mobileBaseUrl,
              tokenProvider = { token }
            )
          }

          fun logout() {
            token = null
            currentScreen = Screen.EventList
            seatMapRefreshKey++ // por las dudas refresca
          }

          // ✅ si no hay token: Login
          if (token == null) {
            LoginScreen(
              authApi = authApi,
              onLoginSuccess = { newToken ->
                token = newToken
                currentScreen = Screen.EventList
              }
            )
            return@Surface
          }

          when (val screen = currentScreen) {

            is Screen.EventList -> {
              EventListScreen(
                api = api,
                onEventClick = { eventId ->
                  currentScreen = Screen.EventDetail(eventId)
                },
                onLogout = { logout() }
              )
            }

            is Screen.EventDetail -> {
              EventDetailScreen(
                api = api,
                eventId = screen.eventId,
                onBack = { currentScreen = Screen.EventList },
                onViewSeats = { eventId ->
                  currentScreen = Screen.SeatMap(eventId, seatMapRefreshKey) // ✅ usa refreshKey
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
                refreshKey = screen.refreshKey,
                onBack = { currentScreen = Screen.EventDetail(screen.eventId) },
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
                onBack = { currentScreen = Screen.SeatMap(screen.eventId, seatMapRefreshKey) },
                onExpired = {
                  // sesión expiró (backend) -> volvemos al detalle y refrescamos mapa
                  seatMapRefreshKey++
                  currentScreen = Screen.EventDetail(screen.eventId)
                },
                onConfirm = { seatsWithPeople ->
                  coroutineScope.launch {
                    try {
                      val personas = seatsWithPeople.map { it.third }

                      val resp = api.sell(
                        eventoId = screen.eventId,
                        fecha = "2025-12-11T18:00:00Z",
                        precioVenta = 1500.0,
                        personas = personas
                      )

                      seatMapRefreshKey++ // ✅ después de vender, refrescar mapa
                      currentScreen = Screen.SaleResult(
                        success = resp.resultado == true,
                        message = resp.descripcion ?: "Sin mensaje"
                      )
                    } catch (e: Exception) {
                      // Si el backend tirara 401, acá podrías hacer logout()
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
