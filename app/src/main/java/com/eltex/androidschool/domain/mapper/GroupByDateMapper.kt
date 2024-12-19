package com.eltex.androidschool.domain.mapper

import com.eltex.androidschool.view.util.datetime.DateSeparators.GroupByDate
import com.eltex.androidschool.view.util.datetime.DateSeparators.Publishable

interface GroupByDateMapper<in From : Publishable, To> {
    fun map(from: List<From>): List<GroupByDate<To>>
}