package fr.corenting.traficparis.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.corenting.traficparis.R
import fr.corenting.traficparis.models.LineState
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.models.api.ApiResponseItem
import fr.corenting.traficparis.traffic.TrafficUiState

private data class DisplayLineItem(
    val type: LineType,
    val state: LineState,
    val name: String,
    val title: String,
    val message: String,
)

private fun mapAndFilter(
    items: List<ApiResponseItem>,
    state: LineState,
    filters: Map<LineType, Boolean>,
): List<DisplayLineItem> {
    return items.mapNotNull { item ->
        val lineType = LineType.entries.find { it.apiName == item.type } ?: return@mapNotNull null
        if (filters[lineType] == false) return@mapNotNull null
        DisplayLineItem(
            type = lineType,
            state = state,
            name = item.name,
            title = item.title,
            message = item.message,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficList(
    uiState: TrafficUiState,
    isRefreshing: Boolean,
    filters: Map<LineType, Boolean>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
    ) {
        when (uiState) {
            is TrafficUiState.Loading -> {
                // PullToRefreshBox handles the indicator
            }

            is TrafficUiState.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.nothing_to_display),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            is TrafficUiState.Success -> {
                val response = uiState.response
                val incidents = remember(response, filters) {
                    mapAndFilter(response.linesWithIncidents, LineState.INCIDENT, filters)
                }
                val works = remember(response, filters) {
                    mapAndFilter(response.linesWithWork, LineState.WORK, filters)
                }

                LazyColumn(Modifier.fillMaxSize()) {
                    // Issues section (always shown)
                    item(key = "header_issues") {
                        SectionHeader(
                            title = stringResource(R.string.issues),
                            modifier = Modifier.animateItem(),
                        )
                    }
                    if (incidents.isEmpty()) {
                        item(key = "no_data_issues") {
                            Text(
                                text = stringResource(R.string.no_data_issues),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .animateItem(),
                            )
                        }
                    } else {
                        items(
                            items = incidents,
                            key = { "incident_${it.type.apiName}_${it.name}" },
                        ) { item ->
                            TrafficLineItem(
                                lineType = item.type,
                                lineName = item.name,
                                title = item.title,
                                message = item.message,
                                modifier = Modifier.animateItem(),
                            )
                        }
                    }

                    // Work section (only if non-empty)
                    if (works.isNotEmpty()) {
                        item(key = "header_work") {
                            SectionHeader(
                                title = stringResource(R.string.work),
                                modifier = Modifier.animateItem(),
                            )
                        }
                        items(
                            items = works,
                            key = { "work_${it.type.apiName}_${it.name}" },
                        ) { item ->
                            TrafficLineItem(
                                lineType = item.type,
                                lineName = item.name,
                                title = item.title,
                                message = item.message,
                                modifier = Modifier.animateItem(),
                            )
                        }
                    }
                }
            }
        }
    }
}
