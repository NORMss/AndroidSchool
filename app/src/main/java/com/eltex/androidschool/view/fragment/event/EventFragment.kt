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
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemoteEventRepository
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.editevent.EditEventFragment
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.fragment.event.adapter.paging.EventPagingAdapter
import com.eltex.androidschool.view.fragment.event.effecthendler.EventEffectHandler
import com.eltex.androidschool.view.fragment.event.reducer.EventReducer
import com.eltex.androidschool.view.fragment.event.reducer.EventReducer.Companion.PAGE_SIZE
import com.eltex.androidschool.view.fragment.newevent.NewEventFragment
import com.eltex.androidschool.view.mapper.EventPagingMapper
import com.eltex.androidschool.view.mapper.EventUiMapper
import com.eltex.androidschool.view.model.EventUi
import com.eltex.androidschool.view.util.toast.toast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventFragment : Fragment() {
    private var adapter = EventPagingAdapter(
        object : EventAdapter.EventListener {
            override fun onLikeClicked(event: EventUi) {
                viewModel.accept(EventMessage.Like(event))
            }

            override fun onShareClicked(event: EventUi) {
                share(event)
            }

            override fun onMoreClicked(event: EventUi, view: View) {
                popupMenuLogic(event, view)
            }

            override fun onPlayClicked(event: EventUi) {
            }

            override fun onParticipateClicked(event: EventUi) {
                viewModel.accept(EventMessage.Participant(event))
            }

            override fun onLoadNextPage() {
                viewModel.accept(EventMessage.LoadNextPage)
            }
        }
    )

    private val mapper = EventPagingMapper()

    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    store = EventStore(
                        reducer = EventReducer(
                            mapper = EventUiMapper(),
                        ),
                        effectHandler = EventEffectHandler(
                            repository = RemoteEventRepository(
                                contentResolver = requireContext().contentResolver,
                                eventApi = (context?.applicationContext as App).eventApi,
                                mediaApi = (context?.applicationContext as App).mediaApi,
                            ),
                        ),
                        initMessages = setOf(
                            EventMessage.Refresh
                        ),
                        initState = EventState(),
                    )
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
            viewModel.accept(EventMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewEventFragment.EVENT_SAVED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(EventMessage.Refresh)
        }

        val view = binding.root

        binding.retryButton.setOnClickListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(EventMessage.Refresh)
        }

        binding.events.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val itemCount = adapter.itemCount
                    val adapterPosition = binding.events.getChildAdapterPosition(view)

                    if (itemCount - PAGE_SIZE / 2 == adapterPosition) {
                        viewModel.accept(EventMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )

        binding.events.adapter = adapter

        binding.events.addItemDecoration(
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
                val errorText = state.emptyError?.getErrorText(requireContext())
                binding.errorText.text = errorText
//                binding.progress.isVisible = state.isEmptyLoading
                binding.swipeRefresh.isRefreshing = state.isRefreshing
                binding.swipeRefresh.isVisible = state.events.isNotEmpty() || state.isEmptyLoading
                errorText?.let { it ->
                    if (state.singleError != null) {
                        val singleErrorText = state.singleError.getErrorText(requireContext())
                        Toast.makeText(
                            requireContext(),
                            singleErrorText,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.accept(EventMessage.HandleError)
                    }
                }
                adapter.submitList(mapper.map(state))
                binding.root.visibility = View.VISIBLE
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun share(event: EventUi) {
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

    private fun popupMenuLogic(event: EventUi, view: View) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        viewModel.accept(EventMessage.Delete(event))
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