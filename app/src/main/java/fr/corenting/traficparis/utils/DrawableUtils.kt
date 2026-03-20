package fr.corenting.traficparis.utils

import android.content.Context
import fr.corenting.traficparis.models.LineType

object DrawableUtils {
    fun getDrawableResId(context: Context, lineType: LineType, lineName: String): Int {
        val name = "${lineType.apiName}_${lineName.lowercase()}"
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }
}
