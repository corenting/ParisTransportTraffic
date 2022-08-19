package fr.corenting.traficparis.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import fr.corenting.traficparis.models.LineType
import java.util.*


object DrawableUtils {
    fun getDrawableForLine(context: Context, lineType: LineType, lineName: String): Drawable? {

        // First try to get specific line logo
        val name: String = when (lineType) {
            LineType.METRO -> "metro_$lineName"
            LineType.RER -> "rer_$lineName"
            LineType.TRAMWAY -> "tramway_$lineName"
            LineType.TRANSILIEN -> "transilien_$lineName"
        }

        val identifier = getLineDrawable(context, name)

        // Not found case
        if (identifier == 0) {
            return null
        }

        return ContextCompat.getDrawable(
            context,
            getLineDrawable(context, name)
        )
    }

    private fun getLineDrawable(context: Context, line_name: String): Int {
        val resName = String.format("%s", line_name.lowercase(Locale.ROOT))
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
}