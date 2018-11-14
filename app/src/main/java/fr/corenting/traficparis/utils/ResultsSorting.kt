package fr.corenting.traficparis.utils

import fr.corenting.traficparis.models.*

object ResultsSorting {
    fun convertApiResultsToListItems(results: ApiResponseResults): List<Any> {
        val retList = mutableListOf<Any>()

        // First, add lines with traffic issues
        retList.add(ListTitle(TitleType.TRAFFIC))
        retList.addAll(results.metros
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.METRO, it) })
        retList.addAll(results.rers
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.RER, it) })
        retList.addAll(results.tramways
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.TRAM, it) })

        // Then, add lines with work
        retList.add(ListTitle(TitleType.WORK))
        retList.addAll(results.metros
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.METRO, it) })
        retList.addAll(results.rers
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.RER, it) })
        retList.addAll(results.tramways
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.TRAM, it) })

        // Then, add working lines
        retList.add(ListTitle(TitleType.OK))
        retList.addAll(results.metros
            .filter { it.slug == "normal" }
            .map { convertFromApiModel(TransportType.METRO, it) })
        retList.addAll(results.rers
            .filter { it.slug == "normal" }
            .map { convertFromApiModel(TransportType.RER, it) })
        retList.addAll(results.tramways
            .filter { it.slug == "normal" }
            .map { convertFromApiModel(TransportType.TRAM, it) })

        return retList
    }

    private fun convertFromApiModel(
        type: TransportType,
        apiItem: ApiResponseItem
    ): ListItem {

        val lineState: TrafficState = when {
            apiItem.slug == "normal" -> TrafficState.OK
            apiItem.slug == "normal_trav" -> TrafficState.WORK
            else -> TrafficState.TRAFFIC
        }

        return ListItem(type, lineState, apiItem.line, apiItem.title, apiItem.message)
    }
}