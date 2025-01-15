package com.yousuf.photos.common.events

import androidx.compose.runtime.compositionLocalOf
import com.yousuf.photos.common.di.MessageDelegateScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface MessageDelegate {
    suspend fun showMessage(message: String)

    val events: Flow<SnackbarEvent>
}

class DefaultMessageDelegate @javax.inject.Inject constructor(
    @MessageDelegateScope private val _events: Channel<SnackbarEvent>,
) : MessageDelegate {
    override val events = _events.receiveAsFlow()

    override suspend fun showMessage(message: String) {
        _events.trySend(SnackbarEvent(message))
    }
}

data class SnackbarAction(val name: String, val action: () -> Unit)

data class SnackbarEvent(val message: String, val action: SnackbarAction? = null)

val LocalMessageDelegate = compositionLocalOf<MessageDelegate?> { null }