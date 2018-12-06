package fr.corenting.traficparis.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import fr.corenting.traficparis.models.TransportType
import java.lang.Exception

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
                getDrawableResIdByName(context, name)
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
                getDrawableResIdByName(context, basicName)
            )
        }
    }

    private fun getDrawableResIdByName(context: Context, name: String): Int {
        return context.resources.getIdentifier(
            name.toLowerCase(),
            "drawable", context.packageName
        )
    }
}