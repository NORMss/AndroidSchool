package com.eltex.androidschool.view.fragment.event

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemoteEventRepository
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.common.ObserveAsEvents
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.editevent.EditEventFragment
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.fragment.event.adapter.eventbydate.EventByDateAdapter
import com.eltex.androidschool.view.fragment.newevent.NewEventFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventFragment : Fragment() {
    private var adapter = EventByDateAdapter(
        object : EventAdapter.EventListener {
            override fun onLikeClicked(event: Event) {
                viewModel.likeById(event.id, event.likedByMe)
            }

            override fun onShareClicked(event: Event) {
                share(event)
            }

            override fun onMoreClicked(event: Event, view: View) {
                popupMenuLogic(event, view)
            }

            override fun onPlayClicked(event: Event) {
            }

            override fun onParticipateClicked(event: Event) {
                viewModel.participateById(event.id, event.participatedByMe)
            }

        }
    )

    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    eventRepository = RemoteEventRepository(
                        (context?.applicationContext as App).client
                    ),
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventBinding.inflate(inflater, container, false)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            EditEventFragment.EVENT_EDITED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.loadEvents()
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_SAVED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.loadEvents()
        }

        val view = binding.root

        binding.retryButton.setOnClickListener {
            viewModel.loadEvents()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadEvents()
        }

        binding.eventsByDate.events.adapter = adapter

        binding.eventsByDate.events.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = binding.root.resources.getDimensionPixelSize(R.dimen.list_offset),
                verticalOffset = binding.root.resources.getDimensionPixelSize(R.dimen.list_offset),
            )
        )

        observeViewModelState(binding)

        return view
    }


    private fun observeViewModelState(binding: FragmentEventBinding) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.errorGroup.isVisible = state.isEmptyError
                val errorText = state.status.throwableOtNull?.getErrorText(requireContext())
                binding.errorText.text = errorText
                binding.progress.isVisible = state.isEmptyLoading
                binding.swipeRefresh.isRefreshing = state.isRefreshing
                binding.swipeRefresh.isVisible = state.events.isNotEmpty()
                errorText?.let { it ->
                    if (state.isRefreshError) {
                        Toast.makeText(
                            requireContext(),
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumerError()
                    }
                }
                adapter.submitList(state.eventsByDate)
                binding.root.visibility = View.VISIBLE
                state.toast?.let { toastData ->
                    ObserveAsEvents(toast = toastData, activity = activity)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun share(event: Event) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, event.content)
                .setType("text/plain"),
            null,
        )

        runCatching { startActivity(intent) }
            .onFailure {
                activity?.toast(R.string.app_not_found, false)
            }
    }

    private fun popupMenuLogic(event: Event, view: View) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        viewModel.deleteEvent(event.id)
                        true
                    }

                    R.id.edit -> {
                        requireParentFragment().requireParentFragment().findNavController()
                            .navigate(
                                R.id.action_bottomNavigation_to_editEventFragment,
                                bundleOf(
                                    EditEventFragment.EVENT_ID to event.id,
                                )
                            )
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }
}