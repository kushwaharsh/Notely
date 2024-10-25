package com.example.notesappwithhilt.commonUtils

 sealed class Resource<out T> {
     object Loading : Resource<Nothing>()

     data class Success<out T>(val value: T) : Resource<T>()

     data class Faliure(val throwable: Throwable) :Resource<Nothing>()
}