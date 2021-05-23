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
            TransportType.METRO -> "m$lineName"
            TransportType.RER -> "rer$lineName"
            TransportType.TRAM -> "t$lineName"
        }

        try {
            val drawable = ContextCompat.getDrawable(
                context,
                getLineDrawable(context, name)
            )

            if (drawable != null) {
                return drawable
            } else {
                throw Exception("No drawable found")
            }
        } catch (exception: Exception) {
            val basicName: String = when (lineType) {
                TransportType.METRO -> "metro"
                TransportType.RER -> "rer"
                TransportType.TRAM -> "tram"
            }

            return ContextCompat.getDrawable(
                context,
                getLineDrawable(context, basicName)
            )
        }
    }

    private fun getLineDrawable(context: Context, line_name: String): Int {
        val resName = String.format("line_%s", line_name.lowercase(Locale.ROOT))
        return context.resources.getIdentifier(resName, "drawable", context.packageName)
    }
}