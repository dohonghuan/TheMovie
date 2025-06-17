package com.example.w3wthemovie.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieProductionCountryDto(
    @SerialName("iso_3166_1")
    val iso31661: String,
    @SerialName("name")
    val name: String,
)
