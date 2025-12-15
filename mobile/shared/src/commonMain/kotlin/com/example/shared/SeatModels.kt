package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class SeatDto(
  val fila: Int,
  val columna: Int,
  val estado: String // "LIBRE" o "OCUPADO"
)

@Serializable
data class SeatMapDto(
  val eventoId: Long,
  val filas: Int,
  val columnas: Int,
  val asientos: List<SeatDto>
)

// Modelo “limpio” para la app
data class Seat(
  val row: Int,
  val col: Int,
  val state: SeatState
)

enum class SeatState {
  LIBRE,
  OCUPADO,
  PENDIENTE,
  DESCONOCIDO,
  BLOQUEADO
}

data class SeatMap(
  val eventId: Long,
  val rows: Int,
  val cols: Int,
  val seats: List<Seat>
)
