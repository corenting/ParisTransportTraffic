package fr.corenting.traficparis.utils

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import fr.corenting.traficparis.models.TransportType
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class DrawableUtilsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getDrawableForMetro() {
        for (i in 1..14) {
            val ret = DrawableUtils.getDrawableForLine(this.context, TransportType.METRO, i.toString())
            assertNotNull(ret)
            assertNotEquals(ret, 0x00000000)
        }
    }

    @Test
    fun getDrawableForRer() {
        for (i in listOf("A", "B", "C", "D", "E")) {
            val ret = DrawableUtils.getDrawableForLine(this.context, TransportType.METRO, i)
            assertNotNull(ret)
            assertNotEquals(ret, 0x00000000)
        }
    }

    @Test
    fun getDrawableForTram() {
        for (i in 1..6) {
            val ret = DrawableUtils.getDrawableForLine(this.context, TransportType.METRO, i.toString())
            assertNotNull(ret)
            assertNotEquals(ret, 0x00000000)
        }
    }
}
