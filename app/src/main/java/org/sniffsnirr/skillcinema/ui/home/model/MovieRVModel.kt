package org.sniffsnirr.skillcinema.ui.home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieRVModel(
    val kinopoiskId: Int?=null,
    val imageUrl:String="",
    val movieName:String="",
    val movieGenre:String="",
    val rate:String="",
    val viewed:Boolean=false,
    val isButton:Boolean,
    val categoryDescription:Triple<String,Int?,Int?>?=null
) : Parcelable
