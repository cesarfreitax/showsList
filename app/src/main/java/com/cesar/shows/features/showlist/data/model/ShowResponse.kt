package com.cesar.shows.features.showlist.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShowResponse(
    val show: Show
) : Parcelable {

    @Parcelize
    class Show(
        val _links: Links,
        val averageRuntime: Int,
        val dvdCountry: String? = null,
        val ended: String,
        val externals: Externals,
        val genres: List<String>,
        val id: Int,
        val image: Image,
        val language: String,
        val name: String,
        val network: Network,
        val officialSite: String,
        val premiered: String,
        val rating: Rating,
        val runtime: Int,
        val schedule: Schedule,
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

