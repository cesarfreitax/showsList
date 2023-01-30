package com.cesar.shows.core.network.youtubeapi


import com.cesar.shows.features.showlist.data.model.video.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("youtube/v3/search/")
    fun getSpecificTrailer(
        @Query("q") q: String,
        @Query("key") key: String,
        @Query("type") type: String? = "video",
        @Query("part") part: String? = "snippet",
        @Query("maxResults") maxResults: String? = "1"
    ): Call<VideoResponse?>
}