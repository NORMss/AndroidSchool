package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateTimeStringFormater
import javax.inject.Inject

class EventUiMapper @Inject constructor() : Mapper<Event, EventUi> {
    override fun map(from: Event) = with(from) {
        EventUi(
            id = id,
            content = content,
            author = author,
            published = published,
            datetime = DateTimeStringFormater.dateTimeToString(datetime),
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            likes = likeOwnerIds.size,
            participants = participantsIds.size,
            authorAvatar = authorAvatar,
            attachment = attachment,
            type = type,
            link = link,
        )
    }
}