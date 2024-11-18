package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewPostBinding
import com.eltex.androidschool.ui.EdgeToEdgeHelper
import com.eltex.androidschool.utils.toast.toast

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityNewPostBinding.inflate(layoutInflater)

        setContentView(binding.root)

        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
            val contentText = binding.editText.text?.toString().orEmpty()

            if (contentText.isNotEmpty()) {
                setResult(RESULT_OK, Intent().putExtra(Intent.EXTRA_TEXT, contentText))
                finish()
            } else {
                application.toast(R.string.text_is_empty, false)
            }

            true
        }
    }
}