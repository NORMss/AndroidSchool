package com.eltex.androidschool.utils.toast

import android.app.Activity

fun toastObserve(toast: Pair<Int, Boolean>, activity: Activity?) {
    toast.let { data ->
        activity?.toast(data.first, data.second)
    }
}