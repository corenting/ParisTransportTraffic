package fr.corenting.traficparis.utils

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import fr.corenting.traficparis.models.LineType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.S_V2])
class PersistenceUtilsTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun getDisplayCategoryValueForMetro() {
        PersistenceUtils.setValue(context, LineType.METRO, false)

        assertEquals(PersistenceUtils.getDisplayCategoryValue(context, LineType.METRO), false)
    }

    @Test
    fun getDisplayCategoryValueForRer() {
        PersistenceUtils.setValue(context, LineType.RER, false)

        assertEquals(PersistenceUtils.getDisplayCategoryValue(context, LineType.RER), false)
    }

    @Test
    fun getDisplayCategoryValueForTramway() {
        PersistenceUtils.setValue(context, LineType.TRAMWAY, false)

        assertEquals(PersistenceUtils.getDisplayCategoryValue(context, LineType.TRAMWAY), false)
    }

    @Test
    fun getDisplayCategoryValueForTransilien() {
        PersistenceUtils.setValue(context, LineType.TRANSILIEN, false)

        assertEquals(PersistenceUtils.getDisplayCategoryValue(context, LineType.TRANSILIEN), false)
    }
}
