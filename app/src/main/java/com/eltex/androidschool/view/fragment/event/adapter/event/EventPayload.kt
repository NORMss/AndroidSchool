package com.eltex.androidschool.view.fragment.event.adapter.event

data class EventPayload(
    val likedByMe: Boolean? = null,
    val participatedByMe: Boolean? = null,
    val likeOwnerIds: Set<Long>? = null,
    val participantsIds: Set<Long>? = null,
) {
    fun isNotEmpty(): Boolean =
        likedByMe != null || participatedByMe != null || likeOwnerIds != null || participantsIds != null
}