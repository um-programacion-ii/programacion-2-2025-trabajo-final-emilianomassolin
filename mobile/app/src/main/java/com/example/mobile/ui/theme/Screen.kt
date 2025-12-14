package com.example.mobile.ui.theme

sealed class Screen {
  data object EventList : Screen()
  data class EventDetail(val eventId: Long) : Screen()
}
