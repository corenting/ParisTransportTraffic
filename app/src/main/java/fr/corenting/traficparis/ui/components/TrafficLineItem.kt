package fr.corenting.traficparis.ui.components

import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fr.corenting.traficparis.R
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.utils.DrawableUtils

@Composable
fun TrafficLineItem(
    lineType: LineType,
    lineName: String,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val resId = remember(lineType, lineName) {
        DrawableUtils.getDrawableResId(context, lineType, lineName)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            if (resId != 0) {
                Image(
                    painter = painterResource(resId),
                    contentDescription = stringResource(R.string.line_logo_description),
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Spacer(Modifier.size(24.dp))
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(R.string.line_title, lineName, title),
                    style = MaterialTheme.typography.titleMedium,
                    )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = AnnotatedString.fromHtml(message),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun TrafficLineItemPreview() {
    TrafficLineItem(LineType.RER, lineName = "A", title = "Perturbations multiples", message = "Période : Toute la journée.<br/><br/>Dates : du samedi 21 au dimanche 22 mars.<br/><br/>Le trafic est interrompu entre Paris Gare du Nord et Aérop. C De Gaulle 2 et entre Paris Gare du Nord et Mitry - Claye.<br/><br/>Un service de bus de remplacement est mis en place, avec desserte des gares intermédiaires.<br/><br/>Les horaires du calculateur d&#39;itinéraire tiennent compte des travaux.<br/><br/>Motif : travaux sur le réseau ferroviaire.")
}