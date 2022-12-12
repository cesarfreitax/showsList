package com.cesar.shows.features.showlist.domain.entities

import android.os.Parcelable
import com.cesar.shows.features.showlist.data.model.ShowResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShowEntity(
    val show: Show
) : Parcelable {

    companion object {
        fun mapper(response: ShowResponse) = ShowEntity(
            show = Show(
                _links = response.show._links,
                averageRuntime = response.show.averageRuntime,
                dvdCountry = response.show.dvdCountry,
                ended = response.show.ended,
                externals = response.show.externals,
                genres = response.show.genres,
                id = response.show.id,
                image = response.show.image,
                language = response.show.language,
                name = response.show.name,
                network = response.show.network,
                officialSite = response.show.officialSite,
                premiered = response.show.premiered,
                rating = response.show.rating,
                runtime = response.show.runtime,
                schedule = response.show.schedule,
                status = response.show.status,
                summary = response.show.summary,
                type = response.show.type,
                updated = response.show.updated,
                url = response.show.url,
                webChannel = response.show.webChannel,
                weight = response.show.weight
            )
        )
    }

    @Parcelize
    class Show(
        val _links: ShowResponse.Links,
        val averageRuntime: Int,
        val dvdCountry: String? = null,
        val ended: String,
        val externals: ShowResponse.Externals,
        val genres: List<String>,
        val id: Int,
        val image: ShowResponse.Image,
        val language: String,
        val name: String,
        val network: ShowResponse.Network,
        val officialSite: String,
        val premiered: String,
        val rating: ShowResponse.Rating,
        val runtime: Int,
        val schedule: ShowResponse.Schedule,
        val status: String,
        val summary: String,
        val type: String,
        val updated: Int,
        val url: String,
        val webChannel: String? = null,
        val weight: Int
    ) : Parcelable

    @Parcelize
    class Self(
        val href: String
    ) : Parcelable

    @Parcelize
    class Schedule(
        val days: List<String>,
        val time: String
    ) : Parcelable

    @Parcelize
    class Rating(
        val average: Double
    ) : Parcelable

    @Parcelize
    class Previousepisode(
        val href: String
    ) : Parcelable

    @Parcelize
    class Network(
        val country: Country,
        val id: Int,
        val name: String,
        val officialSite: String
    ) : Parcelable

    @Parcelize
    class Links(
        val previousepisode: Previousepisode,
        val self: Self
    ) : Parcelable

    @Parcelize
    class Image(
        val medium: String,
        val original: String
    ) : Parcelable

    @Parcelize
    class Externals(
        val imdb: String,
        val thetvdb: Int,
        val tvrage: Int
    ) : Parcelable

    @Parcelize
    class Country(
        val code: String,
        val name: String,
        val timezone: String
    ) : Parcelable
}