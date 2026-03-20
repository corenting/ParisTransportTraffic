package fr.corenting.traficparis.models

import androidx.annotation.StringRes
import fr.corenting.traficparis.R

enum class LineType(val apiName: String, @param:StringRes val displayNameResId: Int) {
    METRO("metro", R.string.metro),
    RER("rer", R.string.rer),
    TRANSILIEN("transilien", R.string.transilien),
    TRAMWAY("tramway", R.string.tramway),
    OTHER("other", R.string.other)
}
