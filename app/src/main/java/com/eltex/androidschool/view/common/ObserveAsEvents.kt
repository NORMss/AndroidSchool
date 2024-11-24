package com.eltex.androidschool.view.common

import android.app.Activity
import com.eltex.androidschool.utils.toast.toast

fun ObserveAsEvents(toast: Pair<Int, Boolean>, activity: Activity?) {
    toast.let { data ->
        activity?.toast(data.first, data.second)
    }
}