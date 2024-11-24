package com.eltex.androidschool.view.activity.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityEditPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.common.EdgeToEdgeHelper
import com.eltex.androidschool.utils.constants.IntentPutExtra
import com.eltex.androidschool.utils.toast.toast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityEditPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

        val data = intent.getStringExtra(IntentPutExtra.KEY_POST)

        if (data != null) {
            val post = Json.decodeFromString<Post>(data)

            binding.editText.setText(post.content)

            binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
                val contentText = binding.editText.text?.toString().orEmpty()

                if (contentText.isNotEmpty()) {
                    setResult(
                        RESULT_OK,
                        Intent().putExtra(
                            Intent.EXTRA_TEXT,
                            Json.encodeToString(post.copy(content = contentText))
                        )
                    )
                    finish()
                } else {
                    application.toast(R.string.text_is_empty, false)
                }

                true
            }
            binding.toolbar.setNavigationOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}