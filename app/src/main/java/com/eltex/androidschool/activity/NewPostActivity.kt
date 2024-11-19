package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewPostBinding
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast.toast

class NewPostActivity : AppCompatActivity() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var contentImage: String? = null
        var contentText: String

        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

        handleIncomingIntent(binding)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.contentImage.load(uri) {
                    listener(
                        onStart = { binding.contentImage.visibility = View.VISIBLE },
                        onSuccess = { _, _ ->
                            binding.contentImage.visibility = View.VISIBLE
                            contentImage = uri.toString()
                        },
                        onError = { _, _ -> binding.contentImage.visibility = View.GONE }
                    )
                }
            } else {
                toast(R.string.no_media_selected)
            }
        }

        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            contentText = binding.editText.text?.toString().orEmpty()

            if (contentText.isNotEmpty()) {
                setResult(
                    RESULT_OK,
                    Intent().putStringArrayListExtra(
                        Intent.EXTRA_TEXT,
                        arrayListOf(contentText, contentImage)
                    )
                )
                finish()
            } else {
                application.toast(R.string.text_is_empty, false)
            }

            true
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
}
