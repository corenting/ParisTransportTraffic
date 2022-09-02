package fr.corenting.traficparis.models.list

import fr.corenting.traficparis.models.LineState
import fr.corenting.traficparis.models.LineType

data class ListLineItem (
    val type: LineType, val state: LineState, val name: String,
    val title: String, val message: String
) : ListItemInterface