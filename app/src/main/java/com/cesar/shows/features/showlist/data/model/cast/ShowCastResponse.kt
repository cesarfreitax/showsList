package com.cesar.shows.features.showlist.data.model.cast

class ShowCastResponse(
    val person: Person? = Person(),
    val character: Character? = Character(),
    val self: Boolean? = false,
    val voice: Boolean? = false
) : java.io.Serializable