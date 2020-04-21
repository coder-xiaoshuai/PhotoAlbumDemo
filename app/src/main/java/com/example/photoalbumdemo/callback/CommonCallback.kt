package com.example.photoalbumdemo.callback

interface CommonCallback<T> {
    fun call(t: T)
}