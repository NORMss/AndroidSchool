package com.eltex.androidschool

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.data.repository.InMemoryPostRepository
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel by viewModels<PostViewModel> {
            viewModelFactory {
                addInitializer(PostViewModel::class) {
                    PostViewModel(InMemoryPostRepository())
                }
            }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                withContext(Dispatchers.Main.immediate) {

                }
            }
        }

        viewModel.state
            .onEach {
                if (it.post != null) {
                    bindPost(binding.post, it.post)
                } else {
                    binding.post.root.visibility = View.GONE
                }
            }
            .launchIn(lifecycleScope)

        binding.post.action.likeButton.setOnClickListener {
            viewModel.like()
        }

        binding.post.action.shareButton.setOnClickListener {
            viewModel.share()
        }

        binding.post.header.moreButton.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
                this.toast(R.string.not_implemented, true)
            }

            AttachmentType.AUDIO -> {
                this.toast(R.string.not_implemented, true)
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