package com.cesar.shows.features.showlist.data.model.episode

import java.io.Serializable

class EpisodeResponse(
    val _links: Links? = Links(self = Self(), show = Show()),
    val airdate: String? = "",
    val airstamp: String? = "",
    val airtime: String? = "",
    val id: Int? = 0,
    val image: Image? = Image(),
    val name: String? = "",
    val number: Int? = 0,
    val rating: Rating? = Rating(),
    val runtime: Int? = 0,
    val season: Int? = 0,
    val summary: String? = "",
    val type: String? = "",
    val url: String? = ""
) : Serializable