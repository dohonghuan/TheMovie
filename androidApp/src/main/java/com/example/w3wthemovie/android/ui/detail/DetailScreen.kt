package com.example.w3wthemovie.android.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.w3wthemovie.android.W3WTheMovieTheme
import com.example.w3wthemovie.android.ui.ErrorView
import com.example.w3wthemovie.android.ui.LoadingView
import com.example.w3wthemovie.android.ui.previewdata.MovieDetailParameterProvider
import com.example.w3wthemovie.common.convertToCurrencyWithDollarSign
import com.example.w3wthemovie.common.model.MovieDetail

@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.movieDetail.collectAsStateWithLifecycle()
    when(uiState) {
        is DetailUiState.Loading -> {
            LoadingView()
        }

        is DetailUiState.Error -> {
            ErrorView(message = (uiState as DetailUiState.Error).message)
        }

        is DetailUiState.Success -> {
            SuccessView(
                detail = (uiState as DetailUiState.Success).detail,
                onBack = onBack,
            )
        }
    }
}


@Composable
private fun SuccessView(
    modifier: Modifier = Modifier,
    detail: MovieDetail,
    onBack: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                start = WindowInsets.safeDrawing.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr),
                end = WindowInsets.safeDrawing.asPaddingValues().calculateEndPadding(LayoutDirection.Ltr),
            )
            .verticalScroll(rememberScrollState()),
    ) {

        Box {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .height(224.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.height(224.dp),
                    model = detail.posterPath,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )

                Spacer(Modifier.width(4.dp))

                AsyncImage(
                    modifier = Modifier.height(224.dp),
                    model = detail.backdrop,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(
                        top = 8.dp + WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding(),
                        start = 8.dp + WindowInsets.safeDrawing.asPaddingValues()
                            .calculateStartPadding(
                                LayoutDirection.Ltr
                            ),
                    )
                    .clip(RoundedCornerShape(percent = 50))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .clickable {
                        onBack()
                    }
            ) {
                Image(
                    modifier = Modifier
                        .size(36.dp)
                        .padding(6.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onTertiaryContainer),
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
                .padding(bottom = 8.dp + WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding(),),
        ) {
            Text(
                text = detail.title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = detail.releaseDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = " - ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = detail.runtime,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(Modifier.height(4.dp))

            if (detail.isAdult) {
                Text(
                    text = "‼\uFE0F Adult content",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(4.dp))
            }

            Text(
                text = detail.overview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = "Genres: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.genres,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = "Tagline: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.tagline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Status: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.status,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Rating: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Column {
                        Text(
                            text = detail.voteAverage.toString() + "/10",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        Text(
                            text = detail.voteCount.toString() + " rates",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }


                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Popularity: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.popularity.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Budget: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.budget.convertToCurrencyWithDollarSign(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Revenue: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.revenue.toBigDecimal().toPlainString().convertToCurrencyWithDollarSign(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Office site: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Url(
                                    detail.homepage,
                                    TextLinkStyles(style = SpanStyle(color = Color.Blue))
                                )
                            ) {
                                append(detail.homepage)
                            }

                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Languages: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = detail.spokenLanguages,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Production companies: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = detail.productionCompanies,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DetailSection {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Production countries: ",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = detail.productionCountries,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            detail.belongsToCollection?.let { collection ->
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Belong to collection: ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Text(
                    text = collection.name.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .height(112.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier.height(112.dp),
                        model = collection.poster,
                        contentDescription = null,
                    )

                    Spacer(Modifier.width(4.dp))

                    AsyncImage(
                        modifier = Modifier.height(112.dp),
                        model = collection.backdrop,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.height(8.dp))

        content()

        Spacer(Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview
@Composable
private fun SuccessViewPreview(
    @PreviewParameter(MovieDetailParameterProvider::class) detail: MovieDetail
) {
    W3WTheMovieTheme {
        SuccessView(
            detail = detail,
            onBack = {},
        )
    }
}

