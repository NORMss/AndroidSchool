package com.eltex.androidschool.view.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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

        viewModel.state
            .onEach {
                if (it.event != null) {
                    bindEvent(binding.event, it.event)
                } else {
                    binding.root.visibility = View.GONE
                }
            }
            .launchIn(lifecycleScope)

        binding.event.playButton.setOnClickListener {
            viewModel.play()
        }

        binding.event.action.likeButton.setOnClickListener {
            viewModel.like()
        }

        binding.event.action.shareButton.setOnClickListener {
            viewModel.share()
        }

        binding.event.participate.setOnClickListener {
            viewModel.participate()
        }

        binding.event.header.moreButton.setOnClickListener {
            viewModel.more()
        }
    }


    private fun bindEvent(binding: EventBinding, event: Event) {
        binding.header.monogramText.visibility = View.VISIBLE

        binding.header.username.text = event.author

        binding.header.monogram.apply {
            load(event.authorAvatar) {
                listener(
                    onSuccess = { _, _ ->
                        binding.header.monogramText.visibility = View.GONE
                    },
                )
            }
        }

        when (event.attachment?.type) {
            AttachmentType.IMAGE -> {
                binding.contentImage.apply {
                    load(event.attachment.url) {
                        listener(
                            onSuccess = { _, _ ->
                                binding.contentImage.visibility = View.VISIBLE
                            },
                            onError = { _, _ ->
                                binding.contentImage.visibility = View.GONE
                            }
                        )
                    }
                }
            }

            AttachmentType.VIDEO -> {
//                this.toast(R.string.not_implemented, true)
            }

            AttachmentType.AUDIO -> {
                binding.playButton.apply {
                    binding.playButton.visibility = View.GONE
                }
            }

            null -> {
                binding.contentImage.visibility = View.GONE
                binding.playButton.visibility = View.GONE
            }
        }

        binding.header.monogramText.text = event.author.take(1)
        binding.header.datePublished.text = event.published
        binding.contentText.text = event.content
        binding.onlineStatus.text = getString(
            when (event.type) {
                EventType.ONLINE -> {
                    R.string.online
                }

                EventType.OFFLINE -> {
                    R.string.offline
                }
            }
        )

        binding.datetime.text = event.datetime

        binding.link.apply {
            if (event.link != null) {
                text = event.link
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }

        event.likedByMe.let {
            binding.action.likeButton.isSelected = it
            binding.action.likeButton.text = if (it) "1" else "0"
        }

        event.participatedByMe.let {
            binding.participate.isSelected = it
            binding.participate.text = if (it) "1" else "0"
        }
    }
}