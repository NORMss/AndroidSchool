package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val store: PostStore,
) : ViewModel() {

    val state: StateFlow<PostState> = store.state

    init {
        viewModelScope.launch {
            store.connect()
        }
    }

    fun accept(message: PostMessage) {
        store.accept(message)
    }
}