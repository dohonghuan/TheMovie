package com.example.w3wthemovie.android.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.w3wthemovie.android.W3WTheMovieTheme
import com.example.w3wthemovie.android.ui.CommonResult
import com.example.w3wthemovie.android.ui.ErrorView
import com.example.w3wthemovie.android.ui.LoadingView
import com.example.w3wthemovie.android.ui.previewdata.MoviesParameterProvider
import com.example.w3wthemovie.common.model.Movie

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onClickDetail: (Long) -> Unit,
) {
    val trendMovies by viewModel.trendMovies.collectAsStateWithLifecycle()
    val searchValue by viewModel.searchValue.collectAsStateWithLifecycle()
    val movies by viewModel.movies.collectAsStateWithLifecycle()

    ListScreenView(
        searchView = {
            SearchView(
                searchValue = searchValue,
                onValueChange = viewModel::onSearchValueChange,
            )
        },
        searchValue = searchValue,
        movies = movies,
        trendMovies = trendMovies,
        onClickDetail = onClickDetail,
    )
}

@Composable
private fun ListScreenView(
    modifier: Modifier = Modifier,
    searchView: @Composable () -> Unit,
    searchValue: String,
    movies: CommonResult<List<Movie>>,
    trendMovies: CommonResult<List<Movie>>,
    onClickDetail: (Long) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.height(WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()))

        searchView()

        when(trendMovies) {
            is CommonResult.Loading -> {
                LoadingView()
            }

            else -> {
                when (searchValue) {
                    "" -> {
                        when (trendMovies) {
                            is CommonResult.Success -> {
                                ListMovieView(
                                    headerTitle = "Trending movies",
                                    movies = trendMovies.data,
                                    onClickDetail = onClickDetail,
                                )

                            }

                            is CommonResult.Error -> {
                                ErrorView(message = trendMovies.message)
                            }

                            else -> {
                                // Not happen
                            }
                        }
                    }

                    else -> {
                        when (movies) {
                            is CommonResult.Loading -> {
                                LoadingView()
                            }

                            is CommonResult.Error -> {
                                ErrorView(message = movies.message)
                            }

                            is CommonResult.Success -> {
                                ListMovieView(
                                    headerTitle = "Search results",
                                    movies = movies.data,
                                    onClickDetail = onClickDetail,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListMovieView(
    modifier: Modifier = Modifier,
    headerTitle: String,
    movies: List<Movie>,
    onClickDetail: (Long) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 8.dp + WindowInsets.safeDrawing.asPaddingValues()
                    .calculateStartPadding(LayoutDirection.Ltr),
                end = 8.dp + WindowInsets.safeDrawing.asPaddingValues()
                    .calculateEndPadding(LayoutDirection.Ltr),
            ),
        columns = GridCells.Adaptive(136.dp),
        contentPadding = PaddingValues(
            bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            Text(
                text = headerTitle,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        items(
            items = movies,
            key = { it.id },
        ) { movie ->
            MovieView(
                movie = movie,
                onClick = onClickDetail
            )
        }

        item(
            span = { GridItemSpan(this.maxLineSpan) },
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "No movies left",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    searchValue: String,
    onValueChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = 8.dp + WindowInsets.safeDrawing.asPaddingValues().calculateStartPadding(
                    LayoutDirection.Ltr),
                end = 8.dp + WindowInsets.safeDrawing.asPaddingValues().calculateEndPadding(
                    LayoutDirection.Ltr),
                top = 8.dp,
                bottom = 8.dp,
            ),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth()
                .testTag("search_text_field"),
            value = searchValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchValue.isNotEmpty()) {
                        focusManager.clearFocus()
                    }
                }
            ),
            placeholder = {
                Text(
                    text = "Search movies",
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.testTag("search_leading_icon")
                )
            },
            trailingIcon = {
                if (searchValue.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier.testTag("clear_icon_button"),
                        onClick = {
                            onValueChange("")
                        },
                    ) {
                        Icon(
                            contentDescription = "Clear Search",
                            modifier = Modifier.testTag("clear_trailing_icon"),
                            imageVector = Icons.Filled.Clear,
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun MovieView(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: (Long) -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = {
            onClick(movie.id)
        },
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp),
            model = movie.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color.Yellow,
                )

                Text(
                    text = movie.voteAverage.toString(),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(Modifier.height(4.dp))

            movie.releaseDate?.let { releaseDate ->
                Text(
                    text = releaseDate,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(4.dp))
            }

            Text(
                text = movie.title,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun SearchViewPreview() {
    W3WTheMovieTheme {
        SearchView(
            searchValue = "",
            onValueChange = {},
        )
    }
}

@Preview
@Composable
private fun MovieViewPreview(
    @PreviewParameter(MoviesParameterProvider::class) movies: List<Movie>,
) {
    W3WTheMovieTheme {
        MovieView(
            movie = movies[0],
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ListMovieViewPreview(
    @PreviewParameter(MoviesParameterProvider::class) movies: List<Movie>,
) {
    W3WTheMovieTheme {
        ListMovieView(
            headerTitle = "Search results",
            movies = movies,
            onClickDetail = {},
        )
    }
}
