package com.cesar.shows.features.showlist.data.datasource.service

import com.cesar.shows.features.showlist.data.model.ShowResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowsApiService {

    @GET("shows")
    fun getShows(
        @Query("page") page: Int
    ): Response<ShowResponse>
}