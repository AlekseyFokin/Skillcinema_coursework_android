package org.sniffsnirr.skillcinema.ui.serial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sniffsnirr.skillcinema.entities.serialinfo.SeasonsSerial
import org.sniffsnirr.skillcinema.usecases.GetSerialInfo
import javax.inject.Inject

@HiltViewModel
class SerialSeasonViewModel @Inject constructor(
    val getSerialInfo: GetSerialInfo
) : ViewModel() {

    private val _serialInfo = MutableStateFlow<SeasonsSerial?>(null)
    val serialInfo = _serialInfo.asStateFlow()

    fun getAllSerialData(idMovie:Int) {
        viewModelScope.launch(Dispatchers.IO) {// получение информации о сериале
            kotlin.runCatching {
                getSerialInfo.getAllSeialInfo(idMovie)
            }.fold(
                onSuccess = { _serialInfo.value = it },
                onFailure = { Log.d("SerialInfo", it.message ?: "") }
            )
        }
    }
}


