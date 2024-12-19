package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators

class PostByDateItemCallback : ItemCallback<DateSeparators.GroupByDate<PostUi>>() {
    override fun areItemsTheSame(
        oldItem: DateSeparators.GroupByDate<PostUi>,
        newItem: DateSeparators.GroupByDate<PostUi>
    ): Boolean = oldItem.date == newItem.date

    override fun areContentsTheSame(
        oldItem: DateSeparators.GroupByDate<PostUi>,
        newItem: DateSeparators.GroupByDate<PostUi>
    ): Boolean = oldItem == newItem

    override fun getChangePayload(
        oldItem: DateSeparators.GroupByDate<PostUi>,
        newItem: DateSeparators.GroupByDate<PostUi>
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