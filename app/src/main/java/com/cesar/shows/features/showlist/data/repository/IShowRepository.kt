package com.cesar.shows.features.showlist.data.repository

import com.cesar.shows.core.utils.ResponseWrapper
import com.cesar.shows.features.showlist.domain.entities.ShowEntity

interface IShowRepository {

    suspend fun getShows(page: Int) : ResponseWrapper<ShowEntity>?

}