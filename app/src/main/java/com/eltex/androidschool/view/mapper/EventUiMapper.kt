package com.eltex.androidschool.view.mapper

import com.eltex.androidschool.domain.mapper.Mapper
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.datetime.DateTimeStringFormatter
import javax.inject.Inject

class EventUiMapper @Inject constructor(
    private val dateTimeStringFormatter: DateTimeStringFormatter,
) : Mapper<Event, EventUi> {
    override fun map(from: Event) = with(from) {
        EventUi(
            id = id,
            content = content,
            author = author,
            published = published,
            formattedPublished = dateTimeStringFormatter.format(published),
            datetime = dateTimeStringFormatter.format(datetime),
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