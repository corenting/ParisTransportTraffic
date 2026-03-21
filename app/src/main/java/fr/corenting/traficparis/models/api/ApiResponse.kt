package fr.corenting.traficparis.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("lines_with_works") val linesWithWork: List<ApiResponseItem>,
    @SerialName("lines_with_incidents") val linesWithIncidents: List<ApiResponseItem>,
)
