package com.eltex.androidschool.view.fragment.newevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.fragment.toolbar.ToolbarViewModel
import com.eltex.androidschool.view.model.FileModel
import com.eltex.androidschool.view.util.toast.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.getValue

@AndroidEntryPoint
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

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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

        binding.datePikerButton.setOnClickListener {
            showDateTimePicker(binding)
        }

        binding.takePhoto.setOnClickListener {
            photoUri = getPhotoUri()
            takePhoto.launch(photoUri)
        }

        binding.attachButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.removeFile.setOnClickListener {
            viewModel.removeFile()
        }


        binding.setPlace.setOnClickListener {
            context?.applicationContext?.toast(R.string.not_implemented)
        }

        binding.participate.setOnClickListener {
            context?.applicationContext?.toast(R.string.not_implemented)
        }

        toolbarViewModel.state.onEach {
            viewModel.setText(binding.editText.text?.toString().orEmpty())
            viewModel.setLink(binding.editLink.text?.toString())
            if (it.saveClicked) {
                if (viewModel.state.value.textContent.isNotBlank()) {
                    viewModel.addEvent()
                    when (viewModel.state.value.status) {
                        is Status.Error -> {
                            requireContext().applicationContext.also { context ->
                                viewModel.state.value.status.throwableOtNull?.getErrorText(
                                    context
                                )
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

    private fun observeViewModelState(binding: FragmentNewEventBinding) {
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
                    requireContext().applicationContext.toast(
                        R.string.event_created,
                        false
                    )
                    requireActivity().supportFragmentManager.setFragmentResult(
                        EVENT_SAVED,
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

    private val viewModel by viewModels<NewEventViewModel>()

    private fun showDateTimePicker(binding: FragmentNewEventBinding) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                val timePickerDialog = TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        selectedDate.set(Calendar.MINUTE, minute)

                        val isoDateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val dateFormated =
                            SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
                        val dateFormat = dateFormated.format(selectedDate.time)

                        binding.dateTime.text = dateFormat
                        binding.dateTime.isVisible = true
                        viewModel.setDateTime(isoDateFormat.format(selectedDate.time))
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
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
        const val EVENT_SAVED = "event_saved"
    }
}