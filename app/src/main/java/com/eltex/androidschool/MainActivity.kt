package com.eltex.androidschool

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.databinding.PostBinding
import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1L,
            authorId = 1001L,
            author = "Sergey Bezborodov",
            authorJob = "Android Developer",
            authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
            content = "Сегодня поделюсь своим опытом работы с Jetpack Compose!",
            published = "2024-11-05T14:30:00",
            coordinates = Coordinates(lat = 55.7558, long = 37.6176),
            link = "https://example.com/article",
            mentionedMe = true,
            likedByMe = false,
            attachment = Attachment(
                url = "https://static1.xdaimages.com/wordpress/wp-content/uploads/2021/02/Jetpack-Compose-Beta.jpg?q=50&fit=crop&w=1100&h=618&dpr=1.5",
                type = AttachmentType.IMAGE,
            )
        )

        binding.post.action.likeButton.setOnClickListener {
            post = post.copy(likedByMe = !post.likedByMe)
            bindPost(binding.post, post)
        }

        binding.post.action.shareButton.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }

        binding.post.header.moreButton.setOnClickListener {
            this.toast(R.string.not_implemented, true)
        }


        bindPost(binding.post, post)

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