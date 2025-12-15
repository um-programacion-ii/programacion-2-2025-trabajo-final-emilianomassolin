package com.example.shared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.contentType


class MobileApi(
  private val baseUrl: String,
  private val tokenProvider: () -> String?
) {
  private val client = HttpClient {
    install(ContentNegotiation) {
      json(
        Json {
          ignoreUnknownKeys = true
          isLenient = true
          encodeDefaults = true
        }
      )
    }
  }

  /**
   * GET /api/mobile/eventos
   */
  suspend fun getEvents(): List<EventSummary> {
    val url = "$baseUrl/eventos?page=0&size=1000"

    val response = client.get(url) {
      tokenProvider()?.let { header("Authorization", "Bearer $it") }
    }

    val dtos: List<EventSummaryDto> = response.body()

    return dtos.map {
      EventSummary(
        id = it.eventId,
        title = it.titulo,
        summary = it.resumen,
        dateTime = Instant.parse(it.fecha),
        price = it.precioEntrada,
        typeName = it.tipoNombre,
        typeDescription = it.tipoDescripcion,
        imageUrl = it.imagen,
        address = it.direccion
      )
    }
  }

  /**
   * GET /api/mobile/eventos/{id}
   */
  suspend fun getEventDetail(eventId: Long): EventDetail {
    val url = "$baseUrl/eventos/$eventId"

    val response = client.get(url) {
      tokenProvider()?.let { header("Authorization", "Bearer $it") }
    }

    val dto: EventDetailDto = response.body()

    return EventDetail(
      id = dto.eventId,
      title = dto.titulo,
      description = dto.descripcion,
      rows = dto.filaAsientos,
      columns = dto.columnaAsientos,
      imageUrl = dto.imagen,
      integrantes = dto.integrantes,
      price = dto.precioEntrada
    )
  }
  /**
   * GET /api/mobile/eventos/{id}/asientos
   */
  suspend fun getEventSeats(eventId: Long): SeatMap {
    val url = "$baseUrl/eventos/$eventId/asientos"

    val response = client.get(url) {
      tokenProvider()?.let { header("Authorization", "Bearer $it") }
    }

    val dto: SeatMapDto = response.body()

    val seats = dto.asientos.map { s ->
      Seat(
        row = s.fila,
        col = s.columna,
        state = when (s.estado.uppercase()) {
          "LIBRE" -> SeatState.LIBRE
          "OCUPADO" -> SeatState.OCUPADO
          "PENDIENTE" -> SeatState.PENDIENTE
          "BLOQUEADO" -> SeatState.BLOQUEADO
          else -> SeatState.DESCONOCIDO
        }
      )
    }

    return SeatMap(
      eventId = dto.eventoId,
      rows = dto.filas,
      cols = dto.columnas,
      seats = seats
    )
  }
  /**
   * POST /api/mobile/bloquear-asientos
   */
  suspend fun blockSeats(eventId: Long, seats: List<Pair<Int, Int>>): BlockSeatsResponseDto {
    val url = "$baseUrl/bloquear-asientos"

    val bodyReq = BlockSeatsRequestDto(
      eventoId = eventId,
      asientos = seats.map { (r, c) -> BlockSeatPosDto(fila = r, columna = c) }
    )

    val response = client.post(url) {
      tokenProvider()?.let { header("Authorization", "Bearer $it") }
      contentType(ContentType.Application.Json)
      setBody(bodyReq)
    }

    if (!response.status.isSuccess()) {
      val txt = response.bodyAsText()
      throw Exception("HTTP ${response.status.value}: $txt")
    }

    // si no te interesa la respuesta, igual la parseamos liviana
    return response.body()
  }
  /**
   * GET /api/mobile/seleccion-actual?eventoId=X
   */
  /**
   * GET /api/mobile/seleccion-actual?eventoId=X
   * Devuelve null si no hay selección o si expiró
   */
  suspend fun getCurrentSelection(eventId: Long): CurrentSelectionDto? {
    return try {
      client.get("$baseUrl/seleccion-actual") {
        tokenProvider()?.let { header("Authorization", "Bearer $it") }
        parameter("eventoId", eventId)
      }.body<CurrentSelectionDto>()   // 👈 CLAVE
    } catch (e: Exception) {
      null
    }
  }

  suspend fun sell(
    eventoId: Long,
    fecha: String,
    precioVenta: Double,
    personas: List<String>
  ): SaleResponseDto {
    val bodyReq = SaleRequestDto(
      eventoId = eventoId,
      fecha = fecha,
      precioVenta = precioVenta,
      personas = personas
    )

    return client.post("$baseUrl/venta") {
      tokenProvider()?.let { header("Authorization", "Bearer $it") }
      contentType(ContentType.Application.Json)
      setBody(bodyReq)
    }.body()
  }






}
