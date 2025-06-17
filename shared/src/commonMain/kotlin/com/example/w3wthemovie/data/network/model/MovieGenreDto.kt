package com.example.w3wthemovie.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieGenreDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)
