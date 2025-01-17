package com.eltex.androidschool.view.util.resourcemanager

import android.content.Context

class AndroidResourceManager(private val context: Context) : ResourceManager {
    override fun getString(id: Int): String {
        return context.getString(id)
    }
}