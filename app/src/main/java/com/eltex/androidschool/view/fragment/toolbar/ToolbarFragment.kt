package com.eltex.androidschool.view.fragment.toolbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentToolbarBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ToolbarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentToolbarBinding.inflate(inflater, container, false)

        val saveItem = binding.toolbar.menu.findItem(R.id.save)

        val toolBarViewModel by activityViewModels<ToolbarViewModel>()

        toolBarViewModel.state.onEach {
            saveItem.isVisible = it.showSave
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        saveItem.setOnMenuItemClickListener {
            toolBarViewModel.saveClicked(true)
            true
        }

        return binding.root
    }
}