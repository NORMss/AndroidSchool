package com.eltex.androidschool.view.fragment.editpost

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.model.TestPost
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test

class EditPostViewModelTest {
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
    fun `editPost error then state contains error`() {
        val error = RuntimeException("Edit failed")
        val postRepository = object : PostRepository {
            override fun savePost(post: Post): Single<Post> =
                Single.error(error)
        }
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            schedulersProvider = TestSchedulersProvider,
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
        val postRepository = object : PostRepository {
            override fun savePost(event: Post): Single<Post> =
                Single.fromCallable { TestPost(id = 1L, content = event.content).toDomainPost() }
        }
        val viewModel = EditPostViewModel(
            postRepository = postRepository,
            schedulersProvider = TestSchedulersProvider,
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