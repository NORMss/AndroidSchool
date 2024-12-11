package com.eltex.androidschool.view.fragment.newpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import coil.load
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.databinding.FragmentNewPostBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewPostFragment : Fragment() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val navController = findNavController()

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.contentImage.load(uri) {
                    listener(
                        onStart = { binding.contentImage.visibility = View.VISIBLE },
                        onSuccess = { _, _ ->
                            binding.contentImage.visibility = View.VISIBLE
                            viewModel.setAttachment(uri)
                        },
                        onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                    )
                }
            } else {
                requireContext().applicationContext.toast(R.string.no_media_selected)
            }
        }

        toolbarViewModel.state.onEach {
            viewModel.setText(binding.editText.text?.toString().orEmpty())
            if (it.saveClicked) {
                if (viewModel.state.value.textContent.isNotBlank()) {
                    viewModel.addPost()
                    when (viewModel.state.value.status) {
                        is Status.Error -> {
                            requireContext().applicationContext.also { context ->
                                viewModel.state.value.status.throwableOtNull?.getErrorText(context)
                                    .let { errorText ->
                                        Toast.makeText(
                                            context,
                                            errorText,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }

                        Status.Idle -> {
                            requireContext().applicationContext.toast(R.string.post_created, false)
                            requireActivity().supportFragmentManager.setFragmentResult(
                                POST_SAVED,
                                bundleOf()
                            )
                            navController.navigateUp()
                        }

                        else -> {
                            Unit
                        }
                    }
                } else {
                    requireContext().applicationContext.toast(R.string.text_is_empty, false)
                }
                toolbarViewModel.saveClicked(false)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                it.status.throwableOtNull?.getErrorText(requireContext())?.let { errorText ->
                    if (it.isRefreshError) {
                        Toast.makeText(
                            requireContext(),
                            errorText,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumerError()
                    }
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.attachButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    private val viewModel by viewModels<NewPostViewModel> {
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    postRepository = RemotePostRepository(
                        (requireContext().applicationContext as App).postApi
                    ),
                )
            }
        }
    }

    companion object {
        const val POST_SAVED = "post_saved"
    }
}
