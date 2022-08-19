package fr.corenting.traficparis.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import fr.corenting.traficparis.models.LineType

object PersistenceUtils {

    fun getDisplayCategoryValue(context: Context, lineType: LineType): Boolean {
        return getBoolean(context, """filter_${lineType.apiName}""")
    }


    fun setValue(context: Context, lineType: LineType, newValue: Boolean) {
         setBoolean(context, """filter_${lineType.apiName}""", newValue)
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