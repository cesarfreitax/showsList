package com.cesar.shows.features.showlist.data.model.video

data class VideoResponse(
    val etag: String? = "",
    val items: ArrayList<Item> = arrayListOf(),
    val kind: String? = "",
    val nextPageToken: String? = "",
    val pageInfo: PageInfo? = PageInfo(),
    val regionCode: String? = ""
)