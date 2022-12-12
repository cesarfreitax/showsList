package com.cesar.shows.features.showlist.data.repository

import com.cesar.shows.core.utils.ResponseWrapper
import com.cesar.shows.features.showlist.data.datasource.remote.IShowRemoteDataSource
import com.cesar.shows.features.showlist.domain.entities.ShowEntity
import javax.inject.Inject

class ShowRepositoryImpl @Inject constructor(
    private val remoteDataSource: IShowRemoteDataSource,
) : IShowRepository {

    override suspend fun getShows(page: Int): ResponseWrapper<ShowEntity>? {
        return when (val response = remoteDataSource.getShows(page = page)) {
            is ResponseWrapper.Error -> ResponseWrapper.Error(response.message ?: "")
            is ResponseWrapper.Success -> {
                response.result?.let { result ->
                    val entity = ShowEntity.mapper(response = result)
                    ResponseWrapper.Success(entity)
                }
            }
        }
    }

}