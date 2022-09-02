package fr.corenting.traficparis.models.api

data class ApiResponseItem(
    val type: String,
    val name: String,
    val title: String,
    val message: String
)