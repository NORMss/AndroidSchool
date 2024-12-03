package com.eltex.androidschool.view.fragment.newevent

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
import com.eltex.androidschool.data.repository.RoomEventRepository
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import kotlin.getValue

class NewEventFragment : Fragment() {

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
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)
//        setContentView(binding.root)

//        EdgeToEdgeHelper.enableEdgeToEdge(findViewById(android.R.id.content))

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
                requireContext().applicationContext.toast(R.string.no_media_selected)
            }
        }

//        binding.toolbar.menu.findItem(R.id.save).setOnMenuItemClickListener {
//            viewModel.setText(binding.editText.text?.toString().orEmpty())
//
//            if (viewModel.state.value.textContent.isNotEmpty()) {
////                setResult(
////                    RESULT_OK,
//////                    Intent().putStringArrayListExtra(
//////                        Intent.EXTRA_TEXT,
//////                        arrayListOf(contentText, contentImage)
//////                    )
////                )
//                viewModel.addPost()
//                requireContext().applicationContext.toast(R.string.event_created, false)
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

    private val viewModel by viewModels<NewEventViewModel> {
        viewModelFactory {
            addInitializer(NewEventViewModel::class) {
                NewEventViewModel(
                    eventRepository = RoomEventRepository(
                        eventDao = (requireContext().applicationContext as App).eventDao
                    ),
                )
            }
        }
    }

    private fun handleIncomingIntent(binding: FragmentNewEventBinding) {
//        intent.apply {
//            if (action == Intent.ACTION_SEND && type == "text/plain") {
//                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
//                if (sharedText != null) {
//                    binding.editText.setText(sharedText)
//                }
//            }
//        }
    }
}