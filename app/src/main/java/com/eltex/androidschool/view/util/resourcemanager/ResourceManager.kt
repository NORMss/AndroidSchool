package com.eltex.androidschool.view.util.resourcemanager

import androidx.annotation.StringRes

interface ResourceManager {
    fun getString(@StringRes id: Int): String
}