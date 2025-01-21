package com.eltex.androidschool.domain.mapper

interface Mapper<From, out To> {
    fun map(from: From): To
}