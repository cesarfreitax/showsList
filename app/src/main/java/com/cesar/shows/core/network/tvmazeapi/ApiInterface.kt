package com.cesar.shows.core.network.tvmazeapi

import com.cesar.shows.features.showlist.data.model.cast.ShowCastResponse
import com.cesar.shows.features.showlist.data.model.episode.EpisodeResponse
import com.cesar.shows.features.showlist.data.model.participations.ParticipationsResponse
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

    @GET("/shows/{id}")
    fun getShowById(
        @Path("id") id: Int
    ): Call<ShowResponse?>

    @GET("/shows/{id}/cast")
    fun getCastByShowId(
        @Path("id") id: Int
    ): Call<ArrayList<ShowCastResponse?>>

    @GET("/search/shows")
    fun getShowsBySearch(
        @Query("q") q: String
    ): Call<ArrayList<ShowSearchResponse?>>

    @GET("/shows/{id}/episodes")
    fun getShowEpisodes(@Path("id") id: String): Call<ArrayList<EpisodeResponse?>>

    @GET("/people/{id}/castcredits?embed=show")
    fun getShowsByPersonId(@Path("id") id: Int): Call<ArrayList<ParticipationsResponse?>>
}