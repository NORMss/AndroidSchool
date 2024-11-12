package com.eltex.androidschool.view.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.data.repository.InMemoryPostRepository
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.common.ObserveAsEvents
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(InMemoryPostRepository())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
        setupClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                state.post?.let {
                    bindPost(binding.post, it)
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
        binding.post.action.likeButton.setOnClickListener { viewModel.like() }
        binding.post.action.shareButton.setOnClickListener { viewModel.share() }
        binding.post.header.moreButton.setOnClickListener { viewModel.more() }
    }

    private fun bindPost(binding: PostBinding, post: Post) {
        val header = binding.header
        val action = binding.action

        binding.contentImage.visibility = View.VISIBLE
        header.monogramText.visibility = View.VISIBLE
        header.username.text = post.author

        header.monogram.load(post.authorAvatar) {
            listener(onSuccess = { _, _ -> header.monogramText.visibility = View.GONE })
        }

        when (post.attachment?.type) {
            AttachmentType.IMAGE -> binding.contentImage.load(post.attachment.url) {
                listener(
                    onSuccess = { _, _ -> binding.contentImage.visibility = View.VISIBLE },
                    onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                )
            }

            AttachmentType.VIDEO -> binding.contentVideo.visibility = View.VISIBLE
            AttachmentType.AUDIO, null -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
            }
        }

        header.monogramText.text = post.author.firstOrNull()?.toString() ?: ""
        header.datePublished.text = post.published
        binding.contentText.text = post.content

        action.likeButton.isSelected = post.likedByMe
        action.likeButton.text = if (post.likedByMe) "1" else "0"
    }
}