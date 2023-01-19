package com.cesar.shows.features.showlist.data.model.show

import java.io.Serializable

class ShowResponse(
    val id: Int? = 0,
    val genres: List<String>? = arrayListOf(),
    val image: Image? = Image(medium = "", original = ""),
    val name: String? = "",
    val rating: Rating? = Rating(average = 0.0),
    val summary: String? = "",
    var isFavorite: Boolean? = false
) : Serializable