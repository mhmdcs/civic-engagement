package com.example.android.politicalpreparedness

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exceptionMessage: String = "Error") : Result<Nothing>()
    object Loading : Result<Nothing>()
}
