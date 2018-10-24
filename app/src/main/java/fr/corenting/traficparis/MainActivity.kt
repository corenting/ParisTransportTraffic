package fr.corenting.traficparis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import fr.corenting.traficparis.ui.main.MainFragment
import android.text.method.LinkMovementMethod
import android.widget.TextView
import fr.corenting.traficparis.utils.MiscUtils


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.about_menu) {
            showAboutPopup()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showAboutPopup() {
        (android.app.AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setIcon(R.mipmap.ic_launcher)
            .setMessage(MiscUtils.htmlToSpanned(getString(R.string.about_text) + BuildConfig.VERSION_NAME))
            .setNegativeButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
            .findViewById(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }
}
