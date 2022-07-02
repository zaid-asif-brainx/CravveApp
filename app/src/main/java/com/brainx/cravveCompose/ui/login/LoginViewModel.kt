package com.brainx.cravveCompose.ui.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brainx.cravveCompose.ui.main.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @javax.inject.Inject constructor(
    loginRepo: LoginRepo,
    handle: SavedStateHandle
) : ViewModel() {

    val reName = handle.getStateFlow(viewModelScope, "rename", "")
    val email = handle.getStateFlow(viewModelScope, "email", "")
    val reEmail = handle.getStateFlow(viewModelScope, "reemail", "")
    val password = handle.getStateFlow(viewModelScope, "password", "")
    val rePassword = handle.getStateFlow(viewModelScope, "repassword", "")

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _events = Channel<ScreenEvent>()
    val events = _events.receiveAsFlow()
    private val _signupEvents = Channel<ScreenEvent>()
    val signupEvents = _signupEvents.receiveAsFlow()

    fun login() {
        viewModelScope.launch(Dispatchers.Default) {
            when {
                email.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter email"))
                password.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter password"))
                else -> {
                    _events.send(ScreenEvent.MoveNextScreen)
                }
            }
        }

    }
    fun signup() {
        viewModelScope.launch(Dispatchers.Default) {
            when {
                reName.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter email"))
                rePassword.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter password"))
                reName.value.isEmpty() -> _events.send(ScreenEvent.ShowToast("Please enter Name"))
                else -> {
                    _signupEvents.send(ScreenEvent.MoveNextScreen)
                }
            }
        }

    }
}

sealed class ScreenEvent {
    class ShowToast(val messageId: String) : ScreenEvent()
    object MoveNextScreen : ScreenEvent()
}


fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    val observer = Observer<T> { value -> if (value != stateFlow.value) stateFlow.value = value }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) liveData.value = value
        }
    }.launchIn(scope)

    return stateFlow
}