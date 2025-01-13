package com.yousuf.photos.common.events

import androidx.compose.runtime.compositionLocalOf
import com.yousuf.photos.di.EventLoggerScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

interface EventsLogger {
    fun logInfo(message: String)
    fun logWarning(message: String)
    fun logError(error: Throwable)
    val events: Flow<LogEvent>
}

class DefaultEventsLogger @Inject constructor(
    @EventLoggerScope private val _events: Channel<LogEvent>,
) : EventsLogger {

    override val events = _events.receiveAsFlow()

    override fun logWarning(message: String) {
        _events.trySend(LogEvent(message, Level.Warning))
    }

    override fun logError(error: Throwable) {
        _events.trySend(LogEvent(error.message ?: "unknown error", Level.Error))
    }

    override fun logInfo(message: String) {
        _events.trySend(LogEvent(message, Level.Info))
    }
}

data class LogEvent(val name: String, val level: Level)

enum class Level {
    Warning,
    Error,
    Info
}

val LocalEventLogger = compositionLocalOf<EventsLogger?> { null }