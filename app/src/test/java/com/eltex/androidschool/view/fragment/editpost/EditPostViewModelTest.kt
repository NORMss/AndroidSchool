package com.eltex.androidschool.view.fragment.editpost

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.repository.PostRepository
import org.junit.Assert.*
import org.junit.Test

class EditPostViewModelTest{
    @Test
    fun setTextTest() {
        val postRepository = object : PostRepository {}
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            schedulersProvider = TestSchedulersProvider,
            postId = 1L,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.post.content

        assertEquals(equals, result)
    }

    @Test
    fun editPostTest() {
        val postRepository = object : PostRepository {}
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            schedulersProvider = TestSchedulersProvider,
            postId = 1L,
        )
        val testText = "test"

        viewModel.setText(testText)

        viewModel.editPost()

        val equals = testText
        val result = viewModel.state.value.post.content

        assertEquals(equals, result)
    }

}