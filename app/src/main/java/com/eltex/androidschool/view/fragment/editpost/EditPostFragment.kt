package com.eltex.androidschool.view.fragment.editpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.eltex.androidschool.databinding.FragmentEditPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import kotlinx.serialization.json.Json
import kotlin.getValue

class EditPostFragment : Fragment() {
    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.setSaveVisible(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.setSaveVisible(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

//        val data = intent.getStringExtra(IntentPutExtra.KEY_POST)
        val data = ""

        if (data != null) {
            val post = Json.decodeFromString<Post>(data)

            binding.editText.setText(post.content)

//            binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
//                val contentText = binding.editText.text?.toString().orEmpty()
//
//                if (contentText.isNotEmpty()) {
////                    setResult(
////                        RESULT_OK,
////                        Intent().putExtra(
////                            Intent.EXTRA_TEXT,
////                            Json.encodeToString(post.copy(content = contentText))
////                        )
////                    )
////                    finish()
//                } else {
//                    requireContext().applicationContext.toast(R.string.text_is_empty, false)
//                }
//
//                true
//            }
//            binding.toolbar.setNavigationOnClickListener {
////                setResult(RESULT_CANCELED)
////                finish()
//            }
        } else {
//            setResult(RESULT_CANCELED)
//            finish()
        }

        return binding.root
    }
}