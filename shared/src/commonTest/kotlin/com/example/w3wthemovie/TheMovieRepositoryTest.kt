package com.example.w3wthemovie

import com.example.w3wthemovie.common.model.dtoMapToMovieDetail
import com.example.w3wthemovie.common.model.dtoMapToMovies
import com.example.w3wthemovie.common.model.entityMapToMovieDetail
import com.example.w3wthemovie.common.model.entityMapToMovies
import com.example.w3wthemovie.common.model.mapToMovieDetailTable
import com.example.w3wthemovie.data.network.model.MovieDetailResponse
import com.example.w3wthemovie.data.network.model.MovieDto
import com.example.w3wthemovie.data.network.model.MovieGenreDto
import com.example.w3wthemovie.data.network.model.MovieProductionCompanyDto
import com.example.w3wthemovie.data.network.model.MovieProductionCountryDto
import com.example.w3wthemovie.data.network.model.MovieResponse
import com.example.w3wthemovie.data.network.model.MovieSpokenLanguagesDto
import com.example.w3wthemovie.data.repository.TheMovieRepository
import com.example.w3wthemovie.data.test.FakeDataStorage
import com.example.w3wthemovie.data.test.FakeMovieDatabase
import com.example.w3wthemovie.data.test.FakeMovieHttpClient
import com.example.w3wthemovie.domain.ValidateCacheTimeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TheMovieRepositoryTest {

    private lateinit var movieHttpClient: FakeMovieHttpClient
    private lateinit var movieDatabase: FakeMovieDatabase
    private lateinit var dataStorage: FakeDataStorage
    private lateinit var validateCacheTimeUseCase: ValidateCacheTimeUseCase

    private lateinit var repository: TheMovieRepository
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val movieResponseSample = MovieResponse(
        page = 1,
        totalResults = 1,
        totalPages = 1,
        results = listOf(
            MovieDto(
                id = 1,
                isAdult = false,
                title = "Movie 1"
            )
        )
    )

    private val movieDetailResponse = MovieDetailResponse(
        id = 1,
        isAdult = false,
        title = "Movie 1",
        budget = 123L,
        genres = listOf(MovieGenreDto(name = "Genre 1", id = 1)),
        homepage = "1.com",
        originalLanguage = "en",
        originalTitle = "Movie 1",
        overview = "Overview 1",
        popularity = 1.0,
        posterPath = "1.jpg",
        productionCompanies = listOf(
            MovieProductionCompanyDto(id = 1L, logoPath = "1.jpg", originCountry = "US", name = "Com 1")
        ),
        productionCountries = listOf(
            MovieProductionCountryDto(iso31661 = "US", name = "US")
        ),
        releaseDate = "2025-01-01",
        revenue = 123.0,
        runtime = 123,
        spokenLanguages = listOf(
            MovieSpokenLanguagesDto(
                englishName = "English",
                iso6391 = "en",
                name = "English"
            )
        ),
        status = "Released",
        tagline = "Tagline 1",
        isHasVideo = false,
        voteAverage = 1.0,
        voteCount = 1L,
    )

    @BeforeTest
    fun setup() {
        movieHttpClient = FakeMovieHttpClient(
            movieResponse = movieResponseSample,
            movieDetailResponse = movieDetailResponse,
        )
        movieDatabase = FakeMovieDatabase()
        dataStorage = FakeDataStorage()
        validateCacheTimeUseCase = ValidateCacheTimeUseCase()

        repository = TheMovieRepository(
            httpClient = movieHttpClient,
            movieDatabase = movieDatabase,
            dataStorage = dataStorage,
            validateCacheTimeUseCase = validateCacheTimeUseCase,
        )
    }

    @AfterTest
    fun tearTown() {
        movieDatabase.clear()
        dataStorage.clear()
    }

    @Test
    fun `fetchTrendingMovies success with cache refresh`() = runTest(testDispatcher) {
        // Verify that when cache is invalid, trending movies are fetched from the API,
        // database is updated, sync time is set, and API results are returned.

        // 1. Set invalid timestamp
        dataStorage.setTrendMovieSyncTime(Clock.System.now().minus(48, DateTimeUnit.HOUR).epochSeconds)

        // 2. Call fetchTrendingMovies
        val trendingMovies = repository.fetchTrendingMovies()

        // 3. Get trending movies from database
        val trendingMoviesDb = movieDatabase.getAllTrendMovie()

        // Verify that database was updated
        assertEquals(trendingMovies, trendingMoviesDb.entityMapToMovies())
    }

    private val trendingMovieExpectSample = listOf(
        MovieDto(
            id = 2,
            isAdult = false,
            title = "Movie 2"
        )
    )

    @Test
    fun `fetchTrendingMovies success from valid cache`() = runTest(testDispatcher) {
        // Verify that when cache is valid, trending movies are fetched from the database,
        // and no API call is made.

        // 1. Set valid timestamp
        dataStorage.setTrendMovieSyncTime(Clock.System.now().minus(12, DateTimeUnit.HOUR).epochSeconds)

        // Set new cache to database
        movieDatabase.clearAndInsertMovies(trendingMovieExpectSample)

        // Call fetchTrendingMovies
        val trendingMovies = repository.fetchTrendingMovies()

        // Verify trendingMovies with database
        assertEquals(trendingMovies, trendingMovieExpectSample.dtoMapToMovies())

        // Verify that database was not updated
        val db = movieDatabase.getAllTrendMovie().entityMapToMovies()
        assertNotEquals(db, movieResponseSample.results.dtoMapToMovies())
    }

    @Test
    fun `fetchMovieDetail success with cache refresh`() = runTest(testDispatcher) {
        // Verify that when movie detail is not cached, it's fetched from the API,
        // database is updated, and the API result is returned.

        // 1. Check if database is empty
        val emptyDatabase = movieDatabase.getDetail(1)
        assertEquals(emptyDatabase, null)

        // 2. Call detail
        val movieDetail = repository.fetchMovieDetail(1)

        // 3. Get detail from database
        val db = movieDatabase.getDetail(1)

        // 4. Check if database was updated
        assertEquals(db?.entityMapToMovieDetail(), movieDetail)
    }

    /**
     * New movie detail with id 1, which was updated
     */
    private val newMovieDetailResponse = MovieDetailResponse(
        id = 1,
        isAdult = false,
        title = "Movie 2",
        budget = 321L,
        genres = listOf(MovieGenreDto(name = "Genre 2", id = 2)),
        homepage = "2.com",
        originalLanguage = "en",
        originalTitle = "Movie 2",
        overview = "Overview 2",
        popularity = 2.0,
        posterPath = "2.jpg",
        productionCompanies = listOf(
            MovieProductionCompanyDto(id = 1L, logoPath = "2.jpg", originCountry = "US", name = "Com 2")
        ),
        productionCountries = listOf(
            MovieProductionCountryDto(iso31661 = "US", name = "US")
        ),
        releaseDate = "2025-01-01",
        revenue = 123.0,
        runtime = 123,
        spokenLanguages = listOf(
            MovieSpokenLanguagesDto(
                englishName = "English",
                iso6391 = "en",
                name = "English"
            )
        ),
        status = "Released",
        tagline = "Tagline 2",
        isHasVideo = false,
        voteAverage = 2.0,
        voteCount = 2L,
    )

    @Test
    fun `fetchMovieDetail success with cache refresh when invalid timestamp`() = runTest(testDispatcher) {
        // Verify that when movie detail is cached with an invalid timestamp, it's fetched from the API,
        // database is updated, and the API result is returned.

        // 1. Insert value into database with invalid timestamp
        val invalidTimeStamp = Clock.System.now().minus(25, DateTimeUnit.HOUR).epochSeconds
        val invalidTimeStampDetail = movieDetailResponse.dtoMapToMovieDetail().mapToMovieDetailTable(invalidTimeStamp)
        movieDatabase.insertOrUpdateDetail(invalidTimeStampDetail)

        // 2. Check if existed
        val detailDatabase = movieDatabase.getDetail(1)
        assertEquals(detailDatabase, invalidTimeStampDetail)

        // 3. Update newMovieDetailResponse to http client
        movieHttpClient.updateMovieDetail(newMovieDetailResponse)

        // 4. Call fetchMovieDetail
        val movieDetail = repository.fetchMovieDetail(1)

        // Verify that data was updated
        assertEquals(movieDetail, newMovieDetailResponse.dtoMapToMovieDetail())
    }

    @Test
    fun `fetchMovieDetail success from valid cache`() = runTest(testDispatcher) {
        // Verify that when movie detail is present in a valid cache, it's fetched from the database,
        // and no API call is made.

        // 1. Insert value into database with valid timestamp
        val validTimeStamp = Clock.System.now().minus(23, DateTimeUnit.HOUR).epochSeconds
        val validTimeStampDetail = movieDetailResponse.dtoMapToMovieDetail().mapToMovieDetailTable(validTimeStamp)
        movieDatabase.insertOrUpdateDetail(validTimeStampDetail)

        // 2. Check if existed
        val detailDatabase = movieDatabase.getDetail(1)
        assertEquals(detailDatabase, validTimeStampDetail)

        // 3. Update newMovieDetailResponse to http client
        movieHttpClient.updateMovieDetail(newMovieDetailResponse)

        // 4. Call fetchMovieDetail
        val movieDetail = repository.fetchMovieDetail(1)

        // Verify that data was not updated and still use cache value
        assertNotEquals(movieDetail, newMovieDetailResponse.dtoMapToMovieDetail())
        assertEquals(movieDetail, detailDatabase?.entityMapToMovieDetail())
    }
}
