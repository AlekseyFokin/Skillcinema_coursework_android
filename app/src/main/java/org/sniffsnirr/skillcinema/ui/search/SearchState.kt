package org.sniffsnirr.skillcinema.ui.search

sealed class SearchState {
    data object Error : SearchState()// статус ошибка
    data object Loading : SearchState() // обновление
    data object AvailableSearch : SearchState()// фильтрация возможна
    data object EmptyData: SearchState()// уже нет данных для фильтрации
}