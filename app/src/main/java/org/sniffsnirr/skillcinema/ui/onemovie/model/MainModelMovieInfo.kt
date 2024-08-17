package org.sniffsnirr.skillcinema.ui.onemovie.model

import org.sniffsnirr.skillcinema.ui.home.model.MovieRVModel

data class MainModelMovieInfo(val category: String,// текст для отображения заголовка в RV
                              val MovieRVModelList: List<Any>,// список фильмов)
)