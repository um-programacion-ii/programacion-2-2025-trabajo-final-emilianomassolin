package com.example.mobile.ui.theme

sealed class Screen {

  object EventList : Screen()

  data class EventDetail(
    val eventId: Long
  ) : Screen()

  data class SeatMap(
    val eventId: Long,
    val refreshKey: Int = 0
  ) : Screen()



  data class Names(
    val eventId: Long,
    val seats: List<Pair<Int, Int>>,
    val expiresAt: String?
  ) : Screen()
  data class SaleResult(
    val success: Boolean,
    val message: String
  ) : Screen()

}
