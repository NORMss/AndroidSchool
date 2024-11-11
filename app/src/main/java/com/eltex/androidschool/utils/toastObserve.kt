package com.eltex.androidschool.utils

import android.app.Activity

fun toastObserve(toast: Pair<Int, Boolean>, activity: Activity?) {
    toast.let { data ->
        activity?.toast(data.first, data.second)
    }
}