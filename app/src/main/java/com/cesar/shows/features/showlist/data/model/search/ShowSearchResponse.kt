package com.cesar.shows.features.showlist.data.model.search

data class ShowSearchResponse(
    val score: Double? = 0.0,
    val show: Show? = Show()
)