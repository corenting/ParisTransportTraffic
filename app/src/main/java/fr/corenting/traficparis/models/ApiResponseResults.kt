package fr.corenting.traficparis.models

data class ApiResponseResults(
    val metros: List<ApiResponseItem>, val rers: List<ApiResponseItem>,
    val tramways: List<ApiResponseItem>
)