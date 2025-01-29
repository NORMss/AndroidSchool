package com.eltex.androidschool.view.model

import android.net.Uri
import com.eltex.androidschool.domain.model.AttachmentType

data class FileModel(
    val uri: Uri,
    val type: AttachmentType,
)
