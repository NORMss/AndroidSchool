package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators

class PostByDateItemCallback : ItemCallback<DateSeparators.GroupByDate<Post>>() {
    override fun areItemsTheSame(
        oldItem: DateSeparators.GroupByDate<Post>,
        newItem: DateSeparators.GroupByDate<Post>
    ): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: DateSeparators.GroupByDate<Post>,
        newItem: DateSeparators.GroupByDate<Post>
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: DateSeparators.GroupByDate<Post>,
        newItem: DateSeparators.GroupByDate<Post>
    ): Any? {
        return PostByDatePayload(
            date = newItem.date.takeIf { it != oldItem.date },
            items = newItem.items.takeIf { it != oldItem.items }
        )
            .takeIf {
                it.isNotEmpty()
            }
    }
}