package com.eltex.androidschool.view.post.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators

class PostByDateAdapter(
    private val clickLikeListener: (post: Post) -> Unit,
    private val clickShareListener: (post: Post) -> Unit,
    private val clickMoreListener: (post: Post) -> Unit,
) : ListAdapter<DateSeparators.GroupByDate<Post>, PostByDateViewHolder>(
    PostByDateItemCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostByDateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentPostBinding.inflate(layoutInflater, parent, false)

        return PostByDateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PostByDateViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                if (it is PostByDatePayload) {
                    holder.bind(it)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PostByDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}