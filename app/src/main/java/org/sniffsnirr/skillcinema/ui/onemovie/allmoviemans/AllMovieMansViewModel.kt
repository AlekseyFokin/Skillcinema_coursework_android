package org.sniffsnirr.skillcinema.ui.onemovie.allmoviemans

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.staff.Staff
import org.sniffsnirr.skillcinema.usecases.GetActorsAndMoviemen
import javax.inject.Inject

@HiltViewModel
class AllMovieMansViewModel @Inject constructor(val getActorsAndMoviemen: GetActorsAndMoviemen) :
    ViewModel() {

    private val _movieMenInfo = MutableStateFlow<List<Staff>>(emptyList())
    val movieMenInfo = _movieMenInfo.asStateFlow()

    fun loadAllMoviemanByMovieId(idMovie: Int, typeOfMovieman:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {// получение и актеров и кинематографистов  по фильму
            kotlin.runCatching {
                getActorsAndMoviemen.getActorsAndMoviemen(idMovie)
            }.fold(
                onSuccess = {
                  if(typeOfMovieman)   {_movieMenInfo.value = it.first}
                    else {_movieMenInfo.value = it.second}
                },
                onFailure = { Log.d("AllActors", it.message ?: "") }
            )
        }
    }
}