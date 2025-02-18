package com.eltex.androidschool.view.fragment.editevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentEditEventBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import com.eltex.androidschool.view.util.toast.toast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

@AndroidEntryPoint
class EditEventFragment : Fragment() {
    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditEventBinding.inflate(inflater, container, false)

        arguments?.getLong(EVENT_ID)?.let { eventId ->
            val viewModel by viewModels<EditEventViewModel>(
                extrasProducer = {
                    defaultViewModelCreationExtras.withCreationCallback<EditEventViewModel.ViewModelFactory> { factory ->
                        factory.create(eventId)
                    }
                }
            )

            viewModel.state.onEach { state ->
                binding.editText.setText(state.event.content)
                state.result?.let {
                    requireContext().applicationContext.toast(
                        R.string.event_edited,
                        false
                    )
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_EDITED,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)


            toolbarViewModel.state.onEach {
                if (it.saveClicked) {
                    viewModel.setText(binding.editText.text?.toString().orEmpty())
                    if (viewModel.state.value.event.content.isNotBlank()) {
                        viewModel.editEvent()
                        when (viewModel.state.value.status) {
                            is Status.Error -> {
                                requireContext().applicationContext.also { context ->
                                    viewModel.state.value.status.throwableOtNull?.getErrorText(
                                        context
                                    )
                                        .let { errorText ->
                                            Toast.makeText(
                                                context,
                                                errorText,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
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
        } ?: {
            requireContext().applicationContext.toast(R.string.post_not_found)
            findNavController().navigateUp()
        }

        return binding.root
    }

    companion object {
        const val EVENT_ID = "event_id"
        const val EVENT_EDITED = "event_edited"

    }
}