package fr.corenting.traficparis.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import fr.corenting.traficparis.models.TransportType
import java.util.*


object DrawableUtils {
    fun getDrawableForLine(context: Context, lineType: TransportType, lineName: String): Drawable? {

        // First try to get specific line logo
        val name: String = when (lineType) {
            TransportType.METRO -> "metro_$lineName"
            TransportType.RER -> "rer_$lineName"
            TransportType.TRAM -> "tram_t$lineName"
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