package com.cesar.shows.features.showlist.data.datasource.remote

import com.cesar.shows.core.network.doRequest
import com.cesar.shows.core.utils.ResponseWrapper
import com.cesar.shows.features.showlist.data.datasource.service.ShowsApiService
import com.cesar.shows.features.showlist.data.model.ShowResponse
import javax.inject.Inject

class ShowRemoteDataSourceImpl @Inject constructor(
    private val apiService: ShowsApiService
) : IShowRemoteDataSource {

    override suspend fun getShows(page: Int): ResponseWrapper<ShowResponse> {
        return doRequest {
            apiService.getShows(page = page)
        }
    }

}