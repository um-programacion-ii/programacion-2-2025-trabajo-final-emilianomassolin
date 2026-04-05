package com.example.shared

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockSeatPosDto(
  val fila: Int,
  val columna: Int
)

@Serializable
data class BlockSeatsRequestDto(
  val eventoId: Long,
  val asientos: List<BlockSeatPosDto>
)

@Serializable
data class BlockSeatResultDto(
  val fila: Int,
  val columna: Int,
  val estado: String? = null
)

@Serializable
data class BlockSeatsResponseDto(
  val eventoId: Long? = null,

  @SerialName("expiracion")
  val expiracion: String? = null,

  @SerialName("expira")
  val expira: String? = null,

  val resultado: Boolean? = null,
  val descripcion: String? = null,
  val asientos: List<BlockSeatResultDto>? = null
) {
  val expiresAt: String?
    get() = expiracion ?: expira
}
