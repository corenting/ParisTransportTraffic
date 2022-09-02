package fr.corenting.traficparis.utils

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import fr.corenting.traficparis.models.LineType
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.S_V2])
class DrawableUtilsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getDrawableForMetro() {
        for (i in 1..14) {
            val ret =
                DrawableUtils.getDrawableForLine(this.context, LineType.METRO, i.toString())
            assertNotNull(ret)
            assertNotEquals(ret, 0)
        }
    }

    @Test
    fun getDrawableForRer() {
        for (i in listOf("A", "B", "C", "D", "E")) {
            val ret = DrawableUtils.getDrawableForLine(this.context, LineType.RER, i)
            assertNotNull(ret)
            assertNotEquals(ret, 0)
        }
    }

    @Test
    fun getDrawableForTramway() {
        for (i in listOf("1", "2", "3a", "3b", "4", "5", "6", "7", "8", "11")) {
            val ret =
                DrawableUtils.getDrawableForLine(this.context, LineType.TRAMWAY, i)
            assertNotNull(ret)
            assertNotEquals(ret, 0)
        }
    }

    @Test
    fun getDrawableForTransilien() {
        for (i in listOf("H", "J", "K", "L", "N", "P", "R", "U")) {
            val ret =
                DrawableUtils.getDrawableForLine(this.context, LineType.TRANSILIEN, i)
            assertNotNull(ret)
            assertNotEquals(ret, 0)
        }
    }
}
