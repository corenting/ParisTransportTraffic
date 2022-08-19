package fr.corenting.traficparis.models.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("lines_with_works") val linesWithWork:  List<ApiResponseItem>,
    @SerializedName("lines_with_incidents") val linesWithIncidents:  List<ApiResponseItem>,
)