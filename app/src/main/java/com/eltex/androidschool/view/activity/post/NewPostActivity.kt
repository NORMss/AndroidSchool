package com.eltex.androidschool.view.activity.post

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.SqlitePostRepository
import com.eltex.androidschool.databinding.ActivityNewPostBinding
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.common.EdgeToEdgeHelper

class NewPostActivity : AppCompatActivity() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

        handleIncomingIntent(binding)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.contentImage.load(uri) {
                    listener(
                        onStart = { binding.contentImage.visibility = View.VISIBLE },
                        onSuccess = { _, _ ->
                            binding.contentImage.visibility = View.VISIBLE
                            viewModel.setAttachment(uri)
                        },
                        onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                    )
                }
            } else {
                toast(R.string.no_media_selected)
            }
        }

        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            viewModel.setText(binding.editText.text?.toString().orEmpty())

            if (viewModel.state.value.textContent.isNotEmpty()) {
                setResult(RESULT_OK)
                viewModel.addPost()
                applicationContext.toast(R.string.post_created, false)
                finish()
            } else {
                applicationContext.toast(R.string.text_is_empty, false)
            }

            true
        }

        binding.toolbar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.attachButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun handleIncomingIntent(binding: ActivityNewPostBinding) {
        intent.apply {
            if (action == Intent.ACTION_SEND && type == "text/plain") {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (sharedText != null) {
                    binding.editText.setText(sharedText)
                }
            }
        }
    }

    private val viewModel by viewModels<NewPostViewModel> {
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    postRepository = SqlitePostRepository(
                        (applicationContext as App).postDao
                    ),
                )
            }
        }
    }
}
