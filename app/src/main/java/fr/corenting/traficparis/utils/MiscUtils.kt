package fr.corenting.traficparis.utils

import android.text.Html
import android.text.Spanned


object MiscUtils {
    @Suppress("DEPRECATION")
    fun htmlToSpanned(source: String): Spanned {
        val s: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
        return s
    }
}