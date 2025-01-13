package com.yousuf.photos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yousuf.photos.R
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.common.events.HandleEventLogger
import com.yousuf.photos.common.events.LocalEventLogger
import com.yousuf.photos.common.events.LocalMessageDelegate
import com.yousuf.photos.common.events.MessageDelegate
import com.yousuf.photos.ui.nav.Destination
import com.yousuf.photos.ui.nav.PhotosNavHost
import com.yousuf.photos.ui.theme.PhotosTheme
import com.yousuf.photos.viewmodel.PhotosViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var eventLogger: EventsLogger

    @Inject
    lateinit var messageDelegate: MessageDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotosTheme {
                val navController = rememberNavController()
                val photosViewModel: PhotosViewModel = hiltViewModel(key = "photos")

                RegisterProviders(eventLogger, messageDelegate) {
                    HandleEventLogger()
                    Scaffold(modifier = Modifier.fillMaxSize(),
                        topBar = { PhotosToolBar(navController, photosViewModel) }
                    ) { innerPadding ->
                        PhotosNavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination = Destination.List.route,
                            photosViewModel = photosViewModel
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosToolBar(
    navController: NavHostController,
    photosViewModel: PhotosViewModel,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    TopAppBar(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary),
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            if (currentDestination == Destination.List.route) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home"
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        navController.navigateUp() // Go back to previous screen
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (currentDestination == Destination.List.route) {
                IconButton(onClick = { photosViewModel.refreshList() }) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        }
    )
}

/**
 * Register providers for the app that can be used by composable via LocalProviders
 */
@Composable
fun RegisterProviders(
    eventLogger: EventsLogger,
    messageDelegate: MessageDelegate,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalEventLogger provides eventLogger,
        LocalMessageDelegate provides messageDelegate,
        content = content
    )
}