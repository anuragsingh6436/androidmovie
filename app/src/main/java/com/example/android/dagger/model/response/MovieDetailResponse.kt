package com.example.android.dagger.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieDetailResponse(
    val adult: Boolean,
    val backdrop_path: String,
    val budget: Int,
    val genres: List<Genres>,
    val id: Int,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String,
    val status: String,
    val title: String,
    val vote_average: Float,
    val production_companies: List<ProductionCompany>,
    val vote_count: Int,
    val release_date:String
) : Parcelable

@Parcelize
data class Genres(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class ProductionCompany(
    val id: Int,
    val logo_path: String,
    val name: String,
    val origin_country: String
) : Parcelable

