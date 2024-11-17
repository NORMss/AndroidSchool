package com.eltex.androidschool.view.event.adapter.event

data class EventPayload(
    val likedByMe: Boolean? = null,
    val participatedByMe: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = likedByMe != null || participatedByMe != null
}