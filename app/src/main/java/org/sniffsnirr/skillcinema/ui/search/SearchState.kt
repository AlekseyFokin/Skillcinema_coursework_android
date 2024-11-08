package org.sniffsnirr.skillcinema.ui.search

sealed class SearchState {
    object Error : SearchState()// статус ошибка
    object Loading : SearchState() // обновление
    object AvailableSearch : SearchState()// поиск возможен
    object SearchDone : SearchState()// поиск выполнен
}