package com.eltex.androidschool.view.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.InMemoryEventRepository
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.databinding.FragmentEventBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.domain.model.EventType
import com.eltex.androidschool.view.common.ObserveAsEvents
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelState()
        setupClickListeners()
    }

    private fun bindEvent(binding: EventBinding, event: Event) {
        val header = binding.header
        val action = binding.action

        header.monogramText.visibility = View.VISIBLE
        header.username.text = event.author

        header.monogram.load(event.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.visibility = View.GONE })
        }

        when (event.attachment?.type) {
            AttachmentType.IMAGE -> binding.contentImage.load(event.attachment.url) {
                listener(
                    onSuccess = { _, _ -> binding.contentImage.visibility = View.VISIBLE },
                    onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                )
            }

            AttachmentType.AUDIO -> {
                binding.playButton.visibility = View.VISIBLE
            }

            AttachmentType.VIDEO -> {}
            null -> {
                binding.contentImage.visibility = View.GONE
                binding.playButton.visibility = View.GONE
            }
        }

        header.monogramText.text = event.author.first().toString()
        header.datePublished.text = event.published
        binding.contentText.text = event.content
        binding.onlineStatus.text =
            getString(if (event.type == EventType.ONLINE) R.string.online else R.string.offline)
        binding.datetime.text = event.datetime

        binding.link.apply {
            text = event.link
            visibility = if (event.link != null) View.VISIBLE else View.GONE
        }

        action.likeButton.isSelected = event.likedByMe
        action.likeButton.text = if (event.likedByMe) "1" else "0"

        binding.participate.isSelected = event.participatedByMe
        binding.participate.text = if (event.participatedByMe) "1" else "0"
    }

    private fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                state.event?.let {
                    bindEvent(binding.event, it)
                    binding.root.visibility = View.VISIBLE
                } ?: run {
                    binding.root.visibility = View.GONE
                }

                state.toast?.let { toastData ->
                    ObserveAsEvents(toast = toastData, activity = activity)
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setupClickListeners() {
        binding.event.playButton.setOnClickListener { viewModel.play() }
        binding.event.action.likeButton.setOnClickListener { viewModel.like() }
        binding.event.action.shareButton.setOnClickListener { viewModel.share() }
        binding.event.participate.setOnClickListener { viewModel.participate() }
        binding.event.header.moreButton.setOnClickListener { viewModel.more() }
    }
}