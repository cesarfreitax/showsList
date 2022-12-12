package com.cesar.shows.features.showlist.di

import com.cesar.shows.features.showlist.data.datasource.remote.IShowRemoteDataSource
import com.cesar.shows.features.showlist.data.datasource.remote.ShowRemoteDataSourceImpl
import com.cesar.shows.features.showlist.data.datasource.service.ShowsApiService
import com.cesar.shows.features.showlist.data.repository.IShowRepository
import com.cesar.shows.features.showlist.data.repository.ShowRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ShowModule {

    @Provides
    @Singleton
    fun providerShowApiService(@Named("CoreRetrofit") retrofit: Retrofit): ShowsApiService =
        retrofit.create(ShowsApiService::class.java)

    @Singleton
    @Provides
    fun providerShowRemoteDataSource(
        apiService: ShowsApiService
    ): IShowRemoteDataSource = ShowRemoteDataSourceImpl(apiService = apiService)

    @Singleton
    @Provides
    fun providerShowRepository(
        remoteDataSource: IShowRemoteDataSource,
    ): IShowRepository = ShowRepositoryImpl(
        remoteDataSource = remoteDataSource
    )
}