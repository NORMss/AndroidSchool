package com.eltex.androidschool.view.fragment.post.adapter.postbydate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.eltex.androidschool.databinding.PostsByDateBinding
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.model.PostUi

class PostByDateAdapter(
    private val postListener: PostAdapter.PostListener,
) : ListAdapter<DateSeparators.GroupByDate<PostUi>, PostByDateViewHolder>(
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