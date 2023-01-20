package com.cesar.shows.features.showlist.data.model.participations

data class ParticipationsResponse(
    val _embedded: Embedded,
    val _links: LinksX,
    val self: Boolean,
    val voice: Boolean
)