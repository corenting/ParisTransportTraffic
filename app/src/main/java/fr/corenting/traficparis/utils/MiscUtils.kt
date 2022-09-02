package fr.corenting.traficparis.utils

import android.os.Build
import android.text.Html
import android.text.Spanned


object MiscUtils {
    @Suppress("DEPRECATION")
    fun htmlToSpanned(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(source)
        }
    }
}