package fr.corenting.traficparis.utils

import fr.corenting.traficparis.models.LineState
import fr.corenting.traficparis.models.LineType
import fr.corenting.traficparis.models.api.ApiResponse
import fr.corenting.traficparis.models.api.ApiResponseItem
import fr.corenting.traficparis.models.list.ListItemInterface
import fr.corenting.traficparis.models.list.ListLineItem
import fr.corenting.traficparis.models.list.ListNoDataItem
import fr.corenting.traficparis.models.list.ListTitleItem

object ListUtils {

    fun mapAndFilterResults(
        result: ApiResponse, displayFilters: Map<LineType, Boolean>
    ): List<ListItemInterface> {

        val linesWithWork = filterLinesList(
            mapLines(result.linesWithWork, LineState.WORK), displayFilters
        )
        val linesWithIncidents = filterLinesList(
            mapLines(result.linesWithIncidents, LineState.INCIDENT), displayFilters
        )

        // If no incidents display a no data item to avoid the app displaying nothing
        val results = mutableListOf<ListItemInterface>()
        results.add(ListTitleItem(LineState.INCIDENT))
        when {
            linesWithIncidents.isNotEmpty() -> results.addAll(linesWithIncidents)
            else -> results.add(ListNoDataItem(LineState.INCIDENT))
        }

        // For work, if empty just don't display it
        if (linesWithWork.isNotEmpty()) {
            results.add(ListTitleItem(LineState.WORK))
            results.addAll(linesWithWork)
        }

        return results
    }

    /**
     * Returns a list of ListLineItem from the [lines] from the API response.
     */
    private fun mapLines(lines: List<ApiResponseItem>, state: LineState): List<ListLineItem> {
        return lines.map { line ->
            ListLineItem(type = LineType.entries.find { it.apiName == line.type }
                ?: LineType.METRO,
                state = state,
                name = line.name,
                message = line.message,
                title = line.title)
        }
    }

    private fun filterLinesList(
        lines: List<ListLineItem>, displayFilters: Map<LineType, Boolean>
    ): List<ListItemInterface> {
        val filteredLines: MutableList<ListLineItem> = lines.toMutableList()

        for (filter in displayFilters) {
            if (!filter.value) {
                filteredLines.removeAll { apiResponseItem ->
                    apiResponseItem.type == filter.key
                }
            }
        }

        return filteredLines
    }
}