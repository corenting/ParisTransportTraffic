package fr.corenting.traficparis.utils

import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.S_V2])
class MiscUtilsTest {
    @Test
    fun htmlToSpanned() {
        val ret = MiscUtils.htmlToSpanned("<p>Test</p>")
        assertNotNull(ret)
        assertEquals(ret.toString(), "Test\n")
    }
}
