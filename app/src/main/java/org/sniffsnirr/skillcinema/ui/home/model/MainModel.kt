package org.sniffsnirr.skillcinema.ui.home.model

// модель данных для rv верхнего уровня
data class MainModel(
    val category: String,// текст для отображения заголовка в RV
    val movieRVModelList: List<MovieRVModel>,// список фильмов
    val categoryDescription: Triple<String, Int?, Int?>,// описание списка фильмов - Метка из KinopoiskApi, если это динамический запрос, то id cтраны и id жанра
    val banner: Boolean// является ли это баннером
)

