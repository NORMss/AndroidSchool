package com.eltex.androidschool.view.fragment.common

import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.databinding.ItemErrorBinding
import com.eltex.androidschool.databinding.PostSkeletonBinding
import com.eltex.androidschool.databinding.SeparatorDateBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.util.datetime.DateSeparators
import com.eltex.androidschool.view.util.resourcemanager.AndroidResourceManager
import kotlinx.datetime.Instant

class ProgressViewHolder(binding: PostSkeletonBinding) :
    RecyclerView.ViewHolder(binding.root)