package com.eltex.androidschool.utils.remote

interface Callback<T> {
    fun onSuccess(data: T)
    fun onError(expectation: Exception)
}