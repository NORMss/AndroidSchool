package com.eltex.androidschool.view.fragment.event.adapter.event

data class EventPayload(
    val likedByMe: Boolean? = null,
    val participatedByMe: Boolean? = null,
    val likes: Int? = null,
    val participants: Int? = null,
) {
    fun isNotEmpty(): Boolean =
        likedByMe != null || participatedByMe != null || likes != null || participants != null
}