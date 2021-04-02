package fr.corenting.traficparis.models.api

data class ApiResponseItem(
    val line: String,
    val slug: String,
    val title: String,
    val message: String
)