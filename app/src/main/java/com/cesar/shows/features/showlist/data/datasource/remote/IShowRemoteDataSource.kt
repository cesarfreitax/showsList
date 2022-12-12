package com.cesar.shows.features.showlist.data.datasource.remote

import com.cesar.shows.core.utils.ResponseWrapper
import com.cesar.shows.features.showlist.data.model.ShowResponse

interface IShowRemoteDataSource {

    /**
     * Get playlist specialties.
     * @param request Object containing the body parameters for requesting the API.
     * @return Pagination data and playlist list.
     */
    suspend fun getShows(page: Int): ResponseWrapper<ShowResponse>

}