package com.cesar.shows.core.network.tvmazeapi

import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.search.ShowSearchResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("/shows")
    fun getShows(
        @Query("page") page: Int
    ): Call<ArrayList<ShowResponse?>>

    @GET("/search/shows")
    fun getShowsBySearch(
        @Query("q") q: String
    ): Call<ArrayList<ShowSearchResponse?>>

    @GET("/shows/{id}/episodes")
    fun getShowEpisodes(@Path("id") id: String): Call<ArrayList<EpisodeResponse?>>
}