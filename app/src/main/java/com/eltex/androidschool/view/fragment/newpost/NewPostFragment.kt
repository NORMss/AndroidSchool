package com.eltex.androidschool.view.fragment.newpost

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.databinding.FragmentNewPostBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import com.eltex.androidschool.view.model.FileModel
import com.eltex.androidschool.view.util.toast.toast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

class NewPostFragment : Fragment() {

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setFile(FileModel(uri, AttachmentType.IMAGE))
                } else {
                    requireContext().applicationContext.toast(R.string.no_media_selected)
                }
            }

        var photoUri: Uri? = null
        val takePhoto =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    photoUri?.let {
                        viewModel.setFile(FileModel(it, AttachmentType.IMAGE))
                    }
                } else {
                    requireContext().applicationContext.toast(R.string.no_media_selected)
                }
            }

        toolbarViewModel.state.onEach {
            viewModel.setText(binding.editText.text?.toString().orEmpty())
            if (it.saveClicked) {
                if (viewModel.state.value.textContent.isNotBlank()) {
                    viewModel.addPost()
                    when (viewModel.state.value.status) {
                        is Status.Error -> {
                            requireContext().applicationContext.also { context ->
                                viewModel.state.value.status.throwableOtNull?.getErrorText(context)
                                    .let { errorText ->
                                        Toast.makeText(
                                            context,
                                            errorText,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }

                        else -> {
                            Unit
                        }
                    }
                } else {
                    requireContext().applicationContext.toast(R.string.text_is_empty, false)
                }
                toolbarViewModel.saveClicked(false)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        observeViewModelState(binding)

        binding.attachButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.takePhoto.setOnClickListener {
            photoUri = getPhotoUri()
            takePhoto.launch(photoUri)
        }

        binding.setPlace.setOnClickListener {
            context?.applicationContext?.toast(R.string.not_implemented)
        }

        binding.participate.setOnClickListener {
            context?.applicationContext?.toast(R.string.not_implemented)
        }


        binding.removeFile.setOnClickListener {
            viewModel.removeFile()
        }

        viewLifecycleOwner.lifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(
                    source: LifecycleOwner,
                    event: Lifecycle.Event
                ) {
                    when (event) {
                        Lifecycle.Event.ON_START -> toolbarViewModel.setSaveVisible(true)
                        Lifecycle.Event.ON_STOP -> toolbarViewModel.setSaveVisible(false)
                        Lifecycle.Event.ON_DESTROY -> source.lifecycle.removeObserver(this)
                        else -> Unit
                    }
                }
            }
        )

        return binding.root
    }

    private val viewModel by viewModels<NewPostViewModel> {
        viewModelFactory {
            addInitializer(NewPostViewModel::class) {
                NewPostViewModel(
                    postRepository = RemotePostRepository(
                        contentResolver = requireContext().contentResolver,
                        postApi = (requireContext().applicationContext as App).postApi,
                        mediaApi = (requireContext().applicationContext as App).mediaApi,
                    ),
                )
            }
        }
    }

    private fun observeViewModelState(binding: FragmentNewPostBinding) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach {
                it.status.throwableOtNull?.getErrorText(requireContext())?.let { errorText ->
                    if (it.isRefreshError) {
                        Toast.makeText(
                            requireContext(),
                            errorText,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumerError()
                    }
                }
                it.result?.let {
                    requireContext().applicationContext.toast(R.string.post_created, false)
                    requireActivity().supportFragmentManager.setFragmentResult(
                        POST_SAVED,
                        bundleOf()
                    )
                    findNavController().navigateUp()
                }
                when (it.file?.type) {
                    AttachmentType.IMAGE -> {
                        binding.attachment.isVisible = true
                        binding.contentImage.setImageURI(it.file.uri)
                    }

                    AttachmentType.VIDEO,
                    AttachmentType.AUDIO,
                    null -> binding.attachment.isVisible = false
                }

            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun getPhotoUri(): Uri {
        val directory = requireContext().cacheDir.resolve("file_picker")
            .apply {
                mkdirs()
            }
        val file = File(directory, "tmp_image_${System.currentTimeMillis()}.png")
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }

    companion object {
        const val POST_SAVED = "post_saved"
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "new_image_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "Pictures/${context?.applicationContext?.getString(R.string.app_name)}"
            )
        }

        return requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
}
