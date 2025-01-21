package com.eltex.androidschool.view.fragment.common

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.resourcemanager.AndroidResourceManager
import kotlinx.datetime.Instant

class SeparatorDateViewHolder(private val binding: SeparatorDateBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val resourceManager by lazy {
        AndroidResourceManager(
            this.binding.root.context.applicationContext
        )
    }

    fun bind(date: Instant) {
        val date = DateSeparators.formatInstantToString(
            instant = date,
            resourceManager = resourceManager
        )
        binding.date.text = date
    }
}