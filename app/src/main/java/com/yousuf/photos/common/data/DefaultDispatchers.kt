package com.yousuf.photos.common.data

import kotlinx.coroutines.CoroutineDispatcher

data class DefaultDispatchers(
    val io: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)