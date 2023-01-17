package com.cesar.shows.features.showlist.data.model.video

data class Item(
    val etag: String? = "",
    val id: Id? = Id(),
    val kind: String? = "",
    val snippet: Snippet? = Snippet()
)