package org.sniffsnirr.skillcinema.ui.serial

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
import org.sniffsnirr.skillcinema.entities.serialinfo.SeasonsSerial
import org.sniffsnirr.skillcinema.usecases.GetSerialInfoUsecase
import javax.inject.Inject

@HiltViewModel
class SerialSeasonViewModel @Inject constructor(
    private val getSerialInfoUsecase: GetSerialInfoUsecase
) : ViewModel() {

    private val _serialInfo = MutableStateFlow<SeasonsSerial?>(null)
    val serialInfo = _serialInfo.asStateFlow()

    private val _error = Channel<Boolean>() // для передачи ошибки соединения с сервисом поиска
    val error = _error.receiveAsFlow()

    fun getAllSerialData(idMovie:Int) {
        viewModelScope.launch(Dispatchers.IO) {// получение информации о сериале
            kotlin.runCatching {
                getSerialInfoUsecase.getAllSeialInfo(idMovie)
            }.fold(
                onSuccess = { _serialInfo.value = it },
                onFailure = {
                    Log.d("Error", "Загрузка инфо о сериале: ${it.message}")
                    _error.send(true)  // показывать диалог с ошибкой - где onFailure
                }
            )
        }
    }
}


