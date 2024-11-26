package com.eltex.androidschool.view.fragment.event

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.SqliteEventRepository
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.utils.constants.IntentPutExtra
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.activity.event.EditEventActivity
import com.eltex.androidschool.view.activity.event.NewEventActivity
import com.eltex.androidschool.view.common.ObserveAsEvents
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.event.adapter.event.EventAdapter
import com.eltex.androidschool.view.fragment.event.adapter.eventbydate.EventByDateAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    private var adapter = EventByDateAdapter(
        object : EventAdapter.EventListener {
            override fun onLikeClicked(event: Event) {
                viewModel.likeById(event.id)
            }

            override fun onShareClicked(event: Event) {
                share(event)
            }

            override fun onMoreClicked(event: Event, view: View) {
                popupMenuLogic(event, view)
            }

            override fun onPlayClicked(event: Event) {
                binding.root.context.toast(R.string.not_implemented, false)
            }

            override fun onParticipateClicked(event: Event) {
                viewModel.participateById(event.id)
            }

        }
    )

    private val viewModel by viewModels<EventViewModel> {
        viewModelFactory {
            addInitializer(EventViewModel::class) {
                EventViewModel(
                    eventRepository = SqliteEventRepository(
                        postDao = (context?.applicationContext as App).eventDao
                    ),
                )
            }
        }
    }

    private val newEventLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
//                val content = result.data?.getStringArrayListExtra(Intent.EXTRA_TEXT)
//                content?.let { viewModel.addEvent(it[0], it[1]) }
            }
        }

    private val editEventLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(Intent.EXTRA_TEXT)
                data?.let {
                    val event = Json.decodeFromString<Event>(data)
                    println(event)
                    viewModel.editEvent(event.id, event.content)
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

        binding.newEvent.setOnClickListener {
            val intent = Intent(requireContext(), NewEventActivity::class.java)
            newEventLauncher.launch(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                Log.d("MyLog", state.eventsByDate.toString())
                adapter.submitList(state.eventsByDate)
                binding.root.visibility = View.VISIBLE
                state.toast?.let { toastData ->
                    ObserveAsEvents(toast = toastData, activity = activity)
                }
            }
            .launchIn(lifecycleScope)
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
                        val intent = Intent(requireContext(), EditEventActivity::class.java).apply {
                            putExtra(IntentPutExtra.KEY_EVENT, Json.encodeToString(event))
                        }
                        editEventLauncher.launch(intent)
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }
}