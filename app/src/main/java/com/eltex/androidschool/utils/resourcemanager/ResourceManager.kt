package com.eltex.androidschool.utils.resourcemanager

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes id: Int): String
}