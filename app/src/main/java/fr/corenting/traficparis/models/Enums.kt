package fr.corenting.traficparis.models

import fr.corenting.traficparis.R

enum class LineType(val apiName: String, val menuFilterId: Int) {
    METRO("metro", R.id.filter_metro),
    RER("rer", R.id.filter_rer),
    TRANSILIEN("transilien", R.id.filter_transilien),
    TRAMWAY("tramway", R.id.filter_tramway),
    OTHER("other", R.id.filter_other)
}

enum class LineState {
    INCIDENT, WORK
}