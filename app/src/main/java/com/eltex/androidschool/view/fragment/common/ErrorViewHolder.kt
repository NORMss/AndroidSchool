package com.eltex.androidschool.view.fragment.common

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.resourcemanager.AndroidResourceManager
import kotlinx.datetime.Instant

class ErrorViewHolder(private val binding: ItemErrorBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(error: Throwable) {
        binding.error.text = error.getErrorText(binding.root.context)
    }
}