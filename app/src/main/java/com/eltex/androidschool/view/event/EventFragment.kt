package com.eltex.androidschool.view.event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.InMemoryEventRepository
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.utils.resourcemanager.AndroidResourceManager
import com.eltex.androidschool.ui.ObserveAsEvents
import com.eltex.androidschool.ui.OffsetDecoration
import com.eltex.androidschool.view.event.adapter.eventbydate.EventByDateAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private var adapter = EventByDateAdapter(
        clickMoreListener = {
            viewModel.more()
        },
        clickLikeListener = {
            viewModel.likeById(it.id)
        },
        clickShareListener = {
            viewModel.share()
        },
        clickPlayListener = {
            viewModel.play()
        },
        clickParticipateListener = {
            viewModel.participateById(it.id)
        }
    )

    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    eventRepository = InMemoryEventRepository(),
                    resourceManager = AndroidResourceManager(binding.root.context),
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        _binding?.eventsByDate?.events?.adapter = adapter

        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            it.data?.getStringExtra(Intent.EXTRA_TEXT)?.let {

            }
        }

        _binding?.eventsByDate?.events?.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = binding.root.resources.getDimensionPixelSize(R.dimen.list_offset),
                verticalOffset = binding.root.resources.getDimensionPixelSize(R.dimen.list_offset),
            )
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelState()
    }

    private fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                state.events.isNotEmpty().let {
                    if (it) {
                        adapter.submitList(state.eventsByDate)
                        binding.root.visibility = View.VISIBLE
                    } else {
                        binding.root.visibility = View.GONE
                    }
                }

                state.toast?.let { toastData ->
                    ObserveAsEvents(toast = toastData, activity = activity)
                }
            }
            .launchIn(lifecycleScope)
    }
}