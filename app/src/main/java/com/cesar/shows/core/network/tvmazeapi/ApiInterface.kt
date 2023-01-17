package com.cesar.shows.core.network.tvmazeapi

import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.show.ShowResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("/shows")
    fun getShows(): Call<ArrayList<ShowResponse?>>

    @GET("/shows/{id}/episodes")
    fun getShowEpisodes(@Path("id") id: String): Call<ArrayList<EpisodeResponse?>>
}