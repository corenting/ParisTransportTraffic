package fr.corenting.traficparis.utils

import fr.corenting.traficparis.models.*

object ResultsUtils {

    fun filterResults(
        results: ApiResponseResults, displayRer: Boolean,
        displayMetro: Boolean, displayTram: Boolean
    ): ApiResponseResults {

        // Filter according to user selection
        return ApiResponseResults(
            results.message,
            if (displayMetro && results.metros != null) results.metros else arrayListOf(),
            if (displayRer && results.rers != null) results.rers else arrayListOf(),
            if (displayTram && results.tramways != null) results.tramways else arrayListOf()
        )
    }

    fun convertApiResultsToListItems(results: ApiResponseResults): List<Any> {
        val retList = mutableListOf<Any>()

        // First, add lines with traffic issues
        val linesWithTrafficIssues = mutableListOf<Any>()
        if (results.metros != null) {
            linesWithTrafficIssues.addAll(results.metros
                .filter { it.slug != "normal" && it.slug != "normal_trav" }
                .map { convertFromApiModel(TransportType.METRO, it) })
        }
        if (results.rers != null) {
            linesWithTrafficIssues.addAll(results.rers
                .filter { it.slug != "normal" && it.slug != "normal_trav" }
                .map { convertFromApiModel(TransportType.RER, it) })
        }
        if (results.tramways != null) {
            linesWithTrafficIssues.addAll(results.tramways
                .filter { it.slug != "normal" && it.slug != "normal_trav" }
                .map { convertFromApiModel(TransportType.TRAM, it) })
        }
        if (linesWithTrafficIssues.size != 0) {
            retList.add(ListTitle(TitleType.TRAFFIC))
            retList.addAll(linesWithTrafficIssues)
        }

        // Then, add lines with work
        val linesWithWork = mutableListOf<Any>()
        if (results.metros != null) {
            linesWithWork.addAll(results.metros
                .filter { it.slug == "normal_trav" }
                .map { convertFromApiModel(TransportType.METRO, it) })
        }
        if (results.rers != null) {
            linesWithWork.addAll(results.rers
                .filter { it.slug == "normal_trav" }
                .map { convertFromApiModel(TransportType.RER, it) })
        }
        if (results.tramways != null) {
            linesWithWork.addAll(results.tramways
                .filter { it.slug == "normal_trav" }
                .map { convertFromApiModel(TransportType.TRAM, it) })
        }
        if (linesWithWork.size > 0) {
            retList.add(ListTitle(TitleType.WORK))
            retList.addAll(linesWithWork)
        }

        // Then, add working lines
        retList.add(ListTitle(TitleType.OK))
        if (results.metros != null) {
            retList.addAll(results.metros
                .filter { it.slug == "normal" }
                .map { convertFromApiModel(TransportType.METRO, it) })
        }
        if (results.rers != null) {
            retList.addAll(results.rers
                .filter { it.slug == "normal" }
                .map { convertFromApiModel(TransportType.RER, it) })
        }
        if (results.tramways != null) {
            retList.addAll(results.tramways
                .filter { it.slug == "normal" }
                .map { convertFromApiModel(TransportType.TRAM, it) })
        }

        return retList
    }

    private fun convertFromApiModel(
        type: TransportType,
        apiItem: ApiResponseItem
    ): ListItem {

        val lineState: TrafficState = when (apiItem.slug) {
            "normal" -> TrafficState.OK
            "normal_trav" -> TrafficState.WORK
            else -> TrafficState.TRAFFIC
        }

        return ListItem(type, lineState, apiItem.line, apiItem.title, apiItem.message)
    }
}