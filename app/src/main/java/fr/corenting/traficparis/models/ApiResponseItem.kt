package fr.corenting.traficparis.models

data class ApiResponseItem(
    val line: String,
    val slug: String,
    val title: String,
    val message: String
)