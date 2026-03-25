package com.almanza.kochappi.data.session

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed interface GlobalEvent {
    data class ShowSnackbar(val message: String) : GlobalEvent
    data object SessionExpired : GlobalEvent
}

@Singleton
class GlobalEventBus @Inject constructor() {

    private val _events = MutableSharedFlow<GlobalEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val events: SharedFlow<GlobalEvent> = _events.asSharedFlow()

    fun emit(event: GlobalEvent) {
        _events.tryEmit(event)
    }
}
