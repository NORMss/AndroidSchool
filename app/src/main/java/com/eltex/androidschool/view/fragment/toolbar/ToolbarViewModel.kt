package com.eltex.androidschool.view.fragment.toolbar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ToolbarViewModel : ViewModel() {
    val state: StateFlow<ToolbarState>
        field = MutableStateFlow(ToolbarState())

    fun setSaveVisible(visible: Boolean) {
        state.update {
            it.copy(
                showSave = visible,
            )
        }
    }

    fun saveClicked(pending: Boolean) {
        state.update {
            it.copy(
                saveClicked = pending,
            )
        }
    }
}