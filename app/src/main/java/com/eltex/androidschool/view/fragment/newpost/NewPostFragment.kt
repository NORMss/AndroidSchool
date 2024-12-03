package com.eltex.androidschool.view.fragment.newpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.load
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RoomPostRepository
import com.eltex.androidschool.databinding.FragmentNewPostBinding
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel

class NewPostFragment : Fragment() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

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
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

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
                requireContext().applicationContext.toast(R.string.no_media_selected)
            }
        }

//        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
//            viewModel.setText(binding.editText.text?.toString().orEmpty())
//
//            if (viewModel.state.value.textContent.isNotEmpty()) {
////                setResult(RESULT_OK)
//                viewModel.addPost()
//                requireContext().applicationContext.toast(R.string.post_created, false)
////                finish()
//            } else {
//                requireContext().applicationContext.toast(R.string.text_is_empty, false)
//            }
//
//            true
//        }
//
//        binding.toolbar.setNavigationOnClickListener {
////            setResult(RESULT_CANCELED)
////            finish()
//        }

        binding.attachButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        return binding.root
    }

    private fun handleIncomingIntent(binding: FragmentNewPostBinding) {
//        intent.apply {
//            if (action == Intent.ACTION_SEND && type == "text/plain") {
//                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
//                if (sharedText != null) {
//                    binding.editText.setText(sharedText)
//                }
//            }
//        }
    }

    private val viewModel by viewModels<NewPostViewModel> {
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    postRepository = RoomPostRepository(
                        (requireContext().applicationContext as App).postDao
                    ),
                )
            }
        }
    }
}
