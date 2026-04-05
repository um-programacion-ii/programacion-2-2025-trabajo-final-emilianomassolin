package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class CurrentSelectionDto(
  val eventoId: Long,
  val asientos: List<BlockSeatPosDto>,
  val expiracion: String? = null
)

