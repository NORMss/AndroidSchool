package com.eltex.androidschool.view.fragment.newevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import coil.load
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemoteEventRepository
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

class NewEventFragment : Fragment() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisible(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisible(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

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
                    viewModel.addEvent()
                    requireContext().applicationContext.toast(R.string.event_created, false)
                    navController.navigateUp()
                } else {
                    requireContext().applicationContext.toast(R.string.text_is_empty, false)
                }
                toolbarViewModel.saveClicked(false)
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

    private val viewModel by viewModels<NewEventViewModel> {
        viewModelFactory {
            addInitializer(NewEventViewModel::class) {
                NewEventViewModel(
                    eventRepository = RemoteEventRepository(
                        (requireContext().applicationContext as App).client
                    ),
                )
            }
        }
    }
}