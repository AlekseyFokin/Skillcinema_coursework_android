package org.sniffsnirr.skillcinema.ui.onemovie.allmoviemans

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.staff.Staff
import org.sniffsnirr.skillcinema.usecases.GetActorsAndMoviemenUsecase
import javax.inject.Inject

@HiltViewModel
class AllMovieMansViewModel @Inject constructor(private val getActorsAndMoviemenUsecase: GetActorsAndMoviemenUsecase) :
    ViewModel() {

    private val _movieMenInfo = MutableStateFlow<List<Staff>>(emptyList())
    val movieMenInfo = _movieMenInfo.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    fun loadAllMoviemanByMovieId(idMovie: Int, typeOfMovieman:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {// получение и актеров и кинематографистов  по фильму
            kotlin.runCatching {
                getActorsAndMoviemenUsecase.getActorsAndMoviemen(idMovie)
            }.fold(
                onSuccess = {
                  if(typeOfMovieman)   {_movieMenInfo.value = it.first}
                    else {_movieMenInfo.value = it.second}
                },
                onFailure = {
                    Log.d("Error", "Загрузка всех кинематографистов: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }
}