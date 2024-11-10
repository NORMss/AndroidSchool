package com.eltex.androidschool.view.post

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.data.repository.InMemoryPostRepository
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Post
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postViewModel by viewModels<PostViewModel> {
            viewModelFactory {
                addInitializer(PostViewModel::class) {
                    PostViewModel(InMemoryPostRepository())
                }
            }
        }

        postViewModel.state
            .onEach {
                if (it.post != null) {
                    bindPost(binding.post, it.post)
                } else {
                    binding.post.root.visibility = View.GONE
                }
            }
            .launchIn(lifecycleScope)

        binding.post.action.likeButton.setOnClickListener {
            postViewModel.like()
        }

        binding.post.action.shareButton.setOnClickListener {
            postViewModel.share()
        }

        binding.post.header.moreButton.setOnClickListener {
//            this.toast(R.string.not_implemented, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindPost(binding: PostBinding, post: Post) {
        binding.header.monogramText.visibility = View.VISIBLE
        binding.contentImage.visibility = View.VISIBLE

        binding.header.username.text = post.author

        binding.header.monogram.apply {
            load(post.authorAvatar) {
                listener(
                    onSuccess = { _, _ ->
                        binding.header.monogramText.visibility = View.GONE
                    },
                )
            }
        }

        when (post.attachment?.type) {
            AttachmentType.IMAGE -> {
                binding.contentImage.apply {
                    load(post.attachment.url) {
                        listener(
                            onSuccess = { _, _ ->
                                binding.contentImage.visibility = View.VISIBLE
                            },
                            onError = { _, _ ->
                                Log.d("BindPost", "Image failed to load, visibility set to GONE")
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
//                this.toast(R.string.not_implemented, true)
            }

            null -> {
                binding.contentImage.visibility = View.GONE
                binding.contentVideo.visibility = View.GONE
            }
        }

        binding.header.monogramText.text = post.author.take(1)
        binding.header.datePublished.text = post.published
        binding.contentText.text = post.content

        post.likedByMe.let {
            binding.action.likeButton.isSelected = it
            binding.action.likeButton.text = if (it) "1" else "0"
        }
    }
}