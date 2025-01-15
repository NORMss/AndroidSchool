package com.eltex.androidschool.view.fragment.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.androidschool.mvi.PostStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(
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