package fr.corenting.traficparis.models

data class ListItem(
    val type: TransportType, val state: TrafficState, val lineName: String,
    val title: String, val stateDescription: String
)