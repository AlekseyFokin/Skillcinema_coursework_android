package org.sniffsnirr.skillcinema.ui.search

sealed class SearchState {
    object Error : SearchState()// статус ошибка
    object Loading : SearchState() // обновление
    object AvailableSearch : SearchState()// фильтрация возможна
    object SearchDone : SearchState()// фильтрация выполнена
    object EmptyData: SearchState()// уже нет данных для фильтрации
}