package fr.corenting.traficparis.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.core.content.ContextCompat
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import fr.corenting.traficparis.BuildConfig
import fr.corenting.traficparis.R

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val aboutHtml = stringResource(R.string.about_text) + BuildConfig.VERSION_NAME
    val drawable = ContextCompat.getDrawable( LocalContext.current, R.mipmap.ic_launcher)

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = rememberDrawablePainter(drawable),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        },
        title = { Text(stringResource(R.string.app_name)) },
        text = {
            Text(AnnotatedString.fromHtml(aboutHtml))
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
    )
}
