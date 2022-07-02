package com.brainx.cravveCompose.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @javax.inject.Inject constructor(
    handle: SavedStateHandle
) : ViewModel() {

    val name = handle.getStateFlow(viewModelScope, "name", "")
    val email = handle.getStateFlow(viewModelScope, "email", "")
    val password = handle.getStateFlow(viewModelScope, "password", "")


    private val _events = Channel<ScreenEvent>()
    val events = _events.receiveAsFlow()

    fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            when {
                name.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter your name"))
                email.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter email"))
                password.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter password"))
                else -> {
                    _events.send(ScreenEvent.MoveNextScreen)
                }
            }
        }

    }
}


