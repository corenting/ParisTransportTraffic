package fr.corenting.traficparis.models

data class RequestResult<T>(val data: T?, val error: Throwable? = null)