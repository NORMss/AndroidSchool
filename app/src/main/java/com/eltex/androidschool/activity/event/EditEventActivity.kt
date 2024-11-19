package com.eltex.androidschool.activity.event

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityEditEventBinding
import com.eltex.androidschool.domain.model.Event
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.constants.IntentPutExtra
import com.eltex.androidschool.utils.toast.toast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EditEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityEditEventBinding.inflate(layoutInflater)

        setContentView(binding.root)

        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

        val data = intent.getStringExtra(IntentPutExtra.KEY_EVENT)

        if (data != null) {
            val event = Json.decodeFromString<Event>(data)

            binding.editText.setText(event.content)

            binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
                val contentText = binding.editText.text?.toString().orEmpty()

                if (contentText.isNotEmpty()) {
                    setResult(
                        RESULT_OK,
                        Intent().putExtra(
                            Intent.EXTRA_TEXT,
                            Json.encodeToString(event.copy(content = contentText))
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