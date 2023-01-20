package com.cesar.shows.features.showlist.data.model.cast

class Character(
    val id: Int? = 0,
    val url: String? = "",
    val name: String? = "",
    val image: Image? = Image(),
    val _links: Links? = Links()
)