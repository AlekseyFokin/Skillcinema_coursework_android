package org.sniffsnirr.skillcinema.ui.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class QueryParams(var country: Int?,
                  var genre: Int?,
                  var order: String,
                  var type:String,
                  var ratingFrom:Float?,
                  var ratingTo:Float?,
                  var yearFrom:Int?,
                  var yearTo:Int?,
                  var onlyUnviewed:Boolean,
                  var keyword:String?
                  ) : Parcelable {
}