package com.eltex.androidschool.model

import kotlinx.datetime.Instant

data class TestGroupByDate<T>(
    val date: Instant = Instant.fromEpochMilliseconds(0),
    val items: List<T> = emptyList()
)
