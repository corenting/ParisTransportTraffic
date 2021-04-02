package fr.corenting.traficparis.models.list

import fr.corenting.traficparis.models.TrafficState
import fr.corenting.traficparis.models.TransportType

data class ListItem(
    val type: TransportType, val state: TrafficState, val lineName: String,
    val title: String, val stateDescription: String
)