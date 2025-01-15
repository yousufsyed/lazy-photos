package com.yousuf.photos.common.events

import android.util.Log
import androidx.compose.runtime.Composable

private const val TAG = "PhotosApp"

@Composable
fun HandleEventLogger() {
    LocalEventLogger.current?.let {
        ObserveAsEvents(flow = it.events) { event ->
            when (event.level) {
                Level.Warning -> Log.w(TAG, event.name)
                Level.Error -> Log.e(TAG, event.name)
                Level.Info -> Log.i(TAG, event.name)
            }
        }
    }
}