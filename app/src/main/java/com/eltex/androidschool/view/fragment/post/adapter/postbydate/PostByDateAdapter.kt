package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.PostsByDateBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.datatime.DateSeparators
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter

class PostByDateAdapter(
    private val postListener: PostAdapter.PostListener,
) : ListAdapter<DateSeparators.GroupByDate<Post>, PostByDateViewHolder>(
    PostByDateItemCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostByDateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PostsByDateBinding.inflate(layoutInflater, parent, false)
        val viewHolder = PostByDateViewHolder(
            postListener = postListener,
            binding = binding,
        )

        return viewHolder
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