package com.eltex.androidschool

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eltex.androidschool.databinding.ActionBinding
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.databinding.EventBinding
import com.eltex.androidschool.model.Attachment
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.model.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = EventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            id = 1L,
            authorId = 1001L,
            author = "Sergey Bezborodov",
            authorJob = "Android Developer",
            authorAvatar = "https://example.com/avatar.jpg",
            content = "Сегодня поделюсь своим опытом работы с Jetpack Compose!",
            published = "2024-11-05T14:30:00",
            coordinates = Coordinates(lat = 55.7558, long = 37.6176),
            link = "https://example.com/article",
            mentionedMe = true,
            likedByMe = false,
            attachment = Attachment(
                url = "https://example.com/image.jpg",
                type = AttachmentType.IMAGE,
            )
        )

        bindEvent(binding, post)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindEvent(binding: EventBinding, post: Post) {
        binding.header.username.setText(post.author)
        binding.header.monogramText.setText(post.author.take(1))
        binding.header.datePublished.setText(post.published)
        binding.contentText.setText(post.content)
        if (post.likedByMe) {
            binding.action.likeButton.text = "1"
        } else {
            binding.action.likeButton.text = "0"
        }
        if (post.mentionedMe) {
            binding.people.text = "1"
        } else {
            binding.people.text = "0"
        }
    }
}