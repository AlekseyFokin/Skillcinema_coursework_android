package org.sniffsnirr.skillcinema.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingMainViewModel : ViewModel() {
    private val _fragmentNumber = MutableStateFlow(ONBORDING_0_FRAGMENT)
    val fragmentNumber = _fragmentNumber.asStateFlow()

    fun setState(fragmentNumber: Int) {
        viewModelScope.launch {
            _fragmentNumber.value = fragmentNumber
        }
    }

    companion object {
        const val ONBORDING_0_FRAGMENT = 0
        const val ONBORDING_1_FRAGMENT = 1
        const val ONBORDING_2_FRAGMENT = 2
        const val LOADING_FRAGMENT = 3
    }
}