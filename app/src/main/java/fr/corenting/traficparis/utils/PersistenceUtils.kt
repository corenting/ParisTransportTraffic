package fr.corenting.traficparis.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import fr.corenting.traficparis.R


object PersistenceUtils {

    private const val RER_KEY = "filter_rer"
    private const val METRO_KEY = "filter_metro"
    private const val TRAM_KEY = "filter_tram"

    fun getDisplayRerValue(context: Context): Boolean {
        return getBoolean(context, RER_KEY)
    }

    fun getDisplayMetroValue(context: Context): Boolean {
        return getBoolean(context, METRO_KEY)
    }

    fun getDisplayTramValue(context: Context): Boolean {
        return getBoolean(context, TRAM_KEY)
    }

    fun setValue(context: Context, prefId: Int, newValue: Boolean) {
        when (prefId) {
            R.id.filter_rer -> setBoolean(context, RER_KEY, newValue)
            R.id.filter_metro -> setBoolean(context, METRO_KEY, newValue)
            R.id.filter_tram -> setBoolean(context, TRAM_KEY, newValue)
        }
    }

    private fun setBoolean(context: Context, prefName: String, newValue: Boolean) {
        val editor =
            context.getSharedPreferences(prefName, MODE_PRIVATE).edit()
        editor.putBoolean(prefName, newValue)
        editor.apply()
    }

    private fun getBoolean(context: Context, prefName: String): Boolean {
        val prefs = context.getSharedPreferences(prefName, MODE_PRIVATE)
        return prefs.getBoolean(prefName, true)
    }
}