package com.example.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.mobile.ui.theme.EventDetailScreen
import com.example.mobile.ui.theme.EventListScreen
import com.example.mobile.ui.theme.Screen
import com.example.shared.MobileApi

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val baseUrl = "http://10.0.2.2:8081/api/mobile"

    // Si tu MobileApi usa tokenProvider:
    val tokenProvider = {
      // tu token del backend
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTc2ODA2NjY5MywiYXV0aCI6IlJPTEVfQURNSU4gUk9MRV9VU0VSIiwiaWF0IjoxNzY1NDc0NjkzLCJ1c2VySWQiOjF9.JseaEBzccfssdew1BH9u-x_JBRAHKj-Z3XtFSDPZaHnpF7ssNemKW7N3azWI6cmi6LxTVTQZdydLzgxp64rUQg"
    }
    val api = MobileApi(baseUrl, tokenProvider)

    setContent {
      MaterialTheme {
        Surface {
          var currentScreen by remember { mutableStateOf<Screen>(Screen.EventList) }

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
                onBack = {
                  currentScreen = Screen.EventList
                }
              )
            }
          }
        }
      }
    }
  }
}
