package com.yousuf.photos.common.di

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.DefaultEventsLogger
import com.yousuf.photos.common.events.DefaultMessageDelegate
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.common.events.LogEvent
import com.yousuf.photos.common.events.MessageDelegate
import com.yousuf.photos.common.events.SnackbarEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    @Singleton
    @EventLoggerScope
    fun providesEventLoggerChannel() = Channel<LogEvent>()

    @Provides
    @Singleton
    @MessageDelegateScope
    fun providesMessageDelegateChannel() = Channel<SnackbarEvent>()

    @Provides
    @Singleton
    fun providesEventLogger(impl: DefaultEventsLogger): EventsLogger = impl

    @Provides
    @Singleton
    fun providesSnackbarDelegate(impl: DefaultMessageDelegate): MessageDelegate = impl

    @Singleton
    @Provides
    fun providesDispatchers(): DefaultDispatchers = DefaultDispatchers(
        default = Dispatchers.Default,
        io = Dispatchers.IO,
        main = Dispatchers.Main
    )

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EventLoggerScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageDelegateScope