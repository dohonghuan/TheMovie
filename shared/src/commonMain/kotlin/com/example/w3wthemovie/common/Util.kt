package com.example.w3wthemovie.common

object W3WTheMovieConst {
    const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
}

fun Long.convertToCurrencyWithDollarSign(): String = "$" + this.toString().convertToCurrency()

fun String.convertToCurrencyWithDollarSign(): String = "$" + this.convertToCurrency()

fun String.convertToCurrency(): String = this.reversed().chunked(3).joinToString(",").reversed()

fun extractMessage(e: Exception): String = e.message.orEmpty().ifEmpty { e.cause?.message.orEmpty() }.ifEmpty { "Oops! Something went wrong" }
