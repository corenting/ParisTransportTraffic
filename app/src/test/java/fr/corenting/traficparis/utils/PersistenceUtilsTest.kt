package fr.corenting.traficparis.utils

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import fr.corenting.traficparis.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PersistenceUtilsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun setRerDisplayValue() {
        PersistenceUtils.setValue(context, R.id.filter_rer, false)
        assertEquals(PersistenceUtils.getDisplayRerValue(context), false)
    }

    @Test
    fun setMetroDisplayValue() {
        PersistenceUtils.setValue(context, R.id.filter_metro, false)
        assertEquals(PersistenceUtils.getDisplayMetroValue(context), false)
    }

    @Test
    fun setTramDisplayValue() {
        PersistenceUtils.setValue(context, R.id.filter_tram, false)
        assertEquals(PersistenceUtils.getDisplayTramValue(context), false)
    }
}
