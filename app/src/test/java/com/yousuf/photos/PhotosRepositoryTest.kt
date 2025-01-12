package com.yousuf.photos

import com.yousuf.photos.common.data.DefaultDispatchers
import com.yousuf.photos.common.events.EventsLogger
import com.yousuf.photos.model.data.PhotoDetails
import com.yousuf.photos.model.network.PhotosService
import com.yousuf.photos.model.repository.DefaultPhotosRepository
import com.yousuf.photos.model.repository.NetworkException
import com.yousuf.photos.model.repository.PhotosRepository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class PhotosRepositoryTest {

    private val mockWebServer = MockWebServer()
    private lateinit var photoService: PhotosService
    private lateinit var photosRepository: PhotosRepository
    private val eventLogger = mockk<EventsLogger>() {
        justRun { logInfo(any()) }
        justRun { logError(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcherProvider = DefaultDispatchers(
        main = UnconfinedTestDispatcher(),
        io = UnconfinedTestDispatcher(),
        default = UnconfinedTestDispatcher()
    )


    @Before
    fun setup() {
        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        mockWebServer.start()
        photoService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .build()
            .create(PhotosService::class.java)

        photosRepository = DefaultPhotosRepository(photoService, eventLogger, dispatcherProvider)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testFetchResultSuccess() {
        runBlocking {
            val response = MockResponse()
                .setResponseCode(200)
                .setBody(photosResponse.trimIndent())

            mockWebServer.enqueue(response)

            val result = photosRepository.fetchPhotos()
            Assert.assertEquals(result, photosMap)
        }
    }

    @Test(expected = NetworkException::class)
    fun testFetchResultFailure() {
        runBlocking {
            MockResponse().setResponseCode(404).also {
                mockWebServer.enqueue(it)
            }
            photosRepository.fetchPhotos()
        }
    }
}

private val photosResponse = """
[
    {
        "format": "jpeg",
        "width": 5000,
        "height": 3333,
        "filename": "0.jpeg",
        "id": 0,
        "author": "Alejandro Escamilla",
        "author_url": "https://unsplash.com/photos/yC-Yzbqy7PY",
        "post_url": "https://unsplash.com/photos/yC-Yzbqy7PY"
    },
    {
        "format": "jpeg",
        "width": 4000,
        "height": 5333,
        "filename": "1.jpeg",
        "id": 1,
        "author": "Alejandro Escamilla",
        "author_url": "https://unsplash.com/photos/LNRyGwIJr5c",
        "post_url": "https://unsplash.com/photos/LNRyGwIJr5c"
    }
]""".trimIndent()

private val photosMap = listOf<PhotoDetails>(
    PhotoDetails(
        3333,
        5000,
        "0.jpeg",
        0,
        "Alejandro Escamilla"
    ),
    PhotoDetails(
        5333,
        4000,
        "1.jpeg",
        1,
        "Alejandro Escamilla"
    )
)