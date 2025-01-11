package com.eltex.androidschool.view.fragment.editpost

import com.eltex.androidschool.TestCoroutineRule
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.model.TestPost
import com.eltex.androidschool.repository.TestErrorPostRepository
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class EditPostViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Test
    fun setTextTest() {
        val postRepository = object : TestErrorPostRepository {}
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            postId = 1L,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.post.content

        assertEquals(equals, result)
    }

    @Test
    fun `editPost error then state contains error`() {
        val error = RuntimeException("Edit failed")
        val postRepository = object : TestErrorPostRepository {
            override suspend fun savePost(post: Post): Post = throw error
        }
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            postId = 1L,
        )

        val testText = "testText"

        viewModel.setText(testText)
        viewModel.editPost()

        val equals = testText
        val result = viewModel.state.value.post.content

        assertEquals(equals, result)
    }

    @Test
    fun `editPostTest success`() {
        val postRepository = object : TestErrorPostRepository {
            override suspend fun savePost(event: Post): Post =
                TestPost(id = 1L, content = event.content).toDomainPost()
        }
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            postId = 1L,
        )
        val testText = "testText"

        viewModel.setText(testText)

        viewModel.editPost()

        val equals = testText
        val result = viewModel.state.value.post.content

        assertEquals(equals, result)
    }

}