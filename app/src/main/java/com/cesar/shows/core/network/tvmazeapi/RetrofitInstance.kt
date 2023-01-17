package com.cesar.shows.core.network.tvmazeapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val tvMazeApiUrl by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiInterface by lazy {
        tvMazeApiUrl.create(ApiInterface::class.java)
    }
}