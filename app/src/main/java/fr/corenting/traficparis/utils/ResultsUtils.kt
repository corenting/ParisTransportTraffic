package fr.corenting.traficparis.utils

import fr.corenting.traficparis.models.*

object ResultsUtils {

    fun filterResults(results: ApiResponseResults, displayRer: Boolean, displayMetro: Boolean,
                      displayTram: Boolean): ApiResponseResults {

        // Filter according to user selection
        return ApiResponseResults(
            if (displayMetro) results.metros else arrayListOf(),
            if (displayRer) results.rers else arrayListOf(),
            if (displayTram) results.tramways else arrayListOf())
    }

    fun convertApiResultsToListItems(results: ApiResponseResults): List<Any> {
        val retList = mutableListOf<Any>()

        // First, add lines with traffic issues
        val linesWithTrafficIssues = mutableListOf<Any>()
        linesWithTrafficIssues.addAll(results.metros
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.METRO, it) })
        linesWithTrafficIssues.addAll(results.rers
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.RER, it) })
        linesWithTrafficIssues.addAll(results.tramways
            .filter { it.slug != "normal" && it.slug != "normal_trav" }
            .map { convertFromApiModel(TransportType.TRAM, it) })
        if (linesWithTrafficIssues.size != 0) {
            retList.add(ListTitle(TitleType.TRAFFIC))
            retList.addAll(linesWithTrafficIssues)
        }


        // Then, add lines with work
        val linesWithWork = mutableListOf<Any>()
        linesWithWork.addAll(results.metros
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.METRO, it) })
        linesWithWork.addAll(results.rers
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.RER, it) })
        linesWithWork.addAll(results.tramways
            .filter { it.slug == "normal_trav" }
            .map { convertFromApiModel(TransportType.TRAM, it) })
        if (linesWithWork.size > 0) {
            retList.add(ListTitle(TitleType.WORK))
            retList.addAll(linesWithWork)
        }

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