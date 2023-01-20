package com.cesar.shows.features.showlist.data.model.cast

import java.io.Serializable

class Person(
    val id: Int? = 0,
    val url: String? = "",
    val name: String? = "",
    val country: Country? = Country(),
    val birthday: String? = "",
    val deathday: Any? = null,
    val gender: String? = "",
    val image: Image? = Image(),
    val updated: Int? = 0,
    val _links: Links? = Links()
) : Serializable