package fr.corenting.traficparis.ui.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.ui.theme.ParisTransportTrafficTheme

@Composable
fun FilterMenu(
    filters: Map<LineType, Boolean>,
    onToggle: (LineType) -> Unit,
    expanded: Boolean,
    onDismiss: () -> Unit,
) {
    ParisTransportTrafficTheme(darkTheme = false) {
        DropdownMenu(expanded = expanded, onDismissRequest = onDismiss) {
            LineType.entries.forEach { lineType ->
                DropdownMenuItem(
                    text = { Text(stringResource(lineType.displayNameResId)) },
                    onClick = { onToggle(lineType) },
                    leadingIcon = {
                        Checkbox(
                            checked = filters[lineType] ?: true,
                            onCheckedChange = null,
                        )
                    },
                )
            }
        }
    }
}
