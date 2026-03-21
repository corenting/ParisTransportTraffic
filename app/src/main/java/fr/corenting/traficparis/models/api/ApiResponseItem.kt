package fr.corenting.traficparis.models.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponseItem(
    val type: String,
    val name: String,
    val title: String,
    val message: String,
)
