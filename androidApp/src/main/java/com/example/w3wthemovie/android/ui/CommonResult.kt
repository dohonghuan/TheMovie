package com.example.w3wthemovie.android.ui

sealed interface CommonResult<T> {
    data class Success<T : Any>(val data: T) : CommonResult<T>

    data class Error<T : Any>(val message: String = "") : CommonResult<T>

    class Loading<T : Any> : CommonResult<T>
}
