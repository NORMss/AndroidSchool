package com.eltex.androidschool.view.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.InMemoryEventRepository
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.view.common.ObserveAsEvents
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.event.adapter.EventAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private var adapter = EventAdapter(
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
                EventViewModel(InMemoryEventRepository())
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

        _binding?.events?.adapter = adapter

        _binding?.events?.addItemDecoration(OffsetDecoration(resources.getDimensionPixelSize(R.dimen.list_offset)))

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
                        adapter.submitList(state.events)
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