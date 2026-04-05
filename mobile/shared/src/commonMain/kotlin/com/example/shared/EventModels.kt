package com.example.shared

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

// Lo que llega del backend
@Serializable
data class EventSummaryDto(
  val eventId: Long,
  val titulo: String,
  val resumen: String,
  val fecha: String,
  val precioEntrada: Double,
  val tipoNombre: String,
  val tipoDescripcion: String,
  val imagen: String? = null,
  val direccion: String? = null
)

// Lo que vamos a usar en la app
data class EventSummary(
  val id: Long,
  val title: String,
  val summary: String,
  val dateTime: Instant,
  val price: Double,
  val typeName: String,
  val typeDescription: String,
  val imageUrl: String?,
  val address: String?
)
// --- NUEVO: detalle de evento (lo que devuelve /eventos/{id})
@Serializable
data class EventDetailDto(
  val eventId: Long,
  val titulo: String,
  val descripcion: String,
  val filaAsientos: Int,
  val columnaAsientos: Int,
  val imagen: String? = null,
  val integrantes: String? = null,
  val precioEntrada: Double
)

// Modelo que vamos a usar en la app
data class EventDetail(
  val id: Long,
  val title: String,
  val description: String,
  val rows: Int,
  val columns: Int,
  val imageUrl: String?,
  val integrantes: String?,
  val price: Double
)
