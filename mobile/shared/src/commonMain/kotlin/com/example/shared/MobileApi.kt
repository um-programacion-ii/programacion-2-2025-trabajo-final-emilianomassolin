package com.example.shared

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant

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
}
