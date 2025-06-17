package com.example.w3wthemovie.data.network

import com.example.w3wthemovie.common.AppConfig
import com.example.w3wthemovie.data.network.model.MovieDetailResponse
import com.example.w3wthemovie.data.network.model.MovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Resource("/trending/movie/day")
private class TrendingMovie()

@Resource("/search/movie")
private class SearchMovie(
    val query: String,
)

@Resource("/movie")
private class MovieDetail() {
    @Resource("{id}")
    class Id(val parent: MovieDetail = MovieDetail(), val id: Long)
}

class DefaultMovieHttpClient: MovieHttpClient {
    private val httpClient =
        HttpClient {
            install(Resources)
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(AppConfig.apiKey, "")
                    }
                }
            }
            install(ContentNegotiation) {
                json(
                    Json(
                        from = Json.Default,
                    ) {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    },
                )
            }
            defaultRequest {
                url(BASE_URL)
                contentType(ContentType.Application.Json)
            }
        }


    override suspend fun fetchTrendingMovies(): MovieResponse = httpClient.get(TrendingMovie()).body()

    override suspend fun searchMovies(searchValue: String): MovieResponse = httpClient.get(SearchMovie(query = searchValue)).body()

    override suspend fun fetchMovieDetail(id: Long): MovieDetailResponse = httpClient.get(MovieDetail.Id(id = id)).body()

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}
