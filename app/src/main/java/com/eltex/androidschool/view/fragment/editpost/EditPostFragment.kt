package com.eltex.androidschool.view.fragment.editpost

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RoomPostRepository
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

class EditPostFragment : Fragment() {
    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

        arguments?.getLong(POST_ID)?.let { postId ->
            val viewModel by viewModels<EditPostViewModel> {
                viewModelFactory {
                    addInitializer(EditPostViewModel::class) {
                        EditPostViewModel(
                            postRepository = RoomPostRepository(
                                (requireContext().applicationContext as App).postDao
                            ),
                            postId = postId,
                        )
                    }
                }
            }

            viewModel.state.onEach { state ->
                binding.editText.setText(state.post.content)
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            toolbarViewModel.state.onEach {
                if (it.saveClicked) {
                    viewModel.setText(binding.editText.text?.toString().orEmpty())
                    if (viewModel.state.value.post.content.isNotBlank()) {
                        viewModel.editPost()
                        requireContext().applicationContext.toast(R.string.post_edited, false)
                        findNavController().navigateUp()
                    } else {
                        requireContext().applicationContext.toast(R.string.text_is_empty, false)
                    }
                    toolbarViewModel.saveClicked(false)
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        } ?: {
            requireContext().applicationContext.toast(R.string.post_not_found)
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event
                ) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(true)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> Unit
                    }
                }
            }
        )

        return binding.root
    }

    companion object {
        const val POST_ID = "post_id"
    }
}