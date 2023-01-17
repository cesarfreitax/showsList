package com.cesar.shows.features.showlist.data.model.video

data class Thumbnails(
    val default: Default? = Default(),
    val high: High? = High(),
    val medium: Medium? = Medium()
)