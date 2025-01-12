package com.eltex.androidschool.view.fragment.newpost

import com.eltex.androidschool.TestCoroutineRule
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.model.TestPost
import com.eltex.androidschool.repository.TestPostRepository
import com.eltex.androidschool.view.common.Status
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class NewPostViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Test
    fun `addPost error then state contains error`() {
        val error = RuntimeException("Save failed")
        val eventRepository = object : TestPostRepository {
            override suspend fun savePost(post: Post): Post = throw error
        }
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
        )

        viewModel.setText("test")
        viewModel.addPost()

        val equals = Status.Error(error)
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }

    @Test
    fun `addPost success`() {
        val eventRepository = object : TestPostRepository {
            override suspend fun savePost(event: Post): Post =
                TestPost(id = 1L, content = "test").toDomainPost()
        }
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
        )

        viewModel.addPost()

        val equals = Status.Idle
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }


    @Test
    fun setTextTest() {
        val eventRepository = object : TestPostRepository {}
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.textContent

        assertEquals(equals, result)
    }
}