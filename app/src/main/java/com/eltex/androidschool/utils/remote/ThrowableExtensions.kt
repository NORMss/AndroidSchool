package com.eltex.androidschool.utils.remote

import android.content.Context
import com.eltex.androidschool.R
import java.io.IOException

fun Throwable.getErrorText(context: Context): String = when (this) {
    is IOException -> context.getString(R.string.network_error)
    else -> context.getString(R.string.unknown_error)
}