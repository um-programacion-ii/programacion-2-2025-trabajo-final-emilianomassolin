package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class SaleRequestDto(
  val eventoId: Long,
  val fecha: String,
  val precioVenta: Double,
  val personas: List<String>
)

@Serializable
data class SaleResponseDto(
  val eventoId: Long? = null,
  val resultado: Boolean? = null,
  val descripcion: String? = null,
  val ventaId: Long? = null,
  val fechaVenta: String? = null
)
