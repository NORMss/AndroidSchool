package com.eltex.androidschool.view.fragment.newpost

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.model.TestPost
import com.eltex.androidschool.view.common.Status
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.*
import org.junit.Test

class NewPostViewModelTest {
    @Test
    fun `addPost error then state contains error`() {
        val error = RuntimeException("Save failed")
        val eventRepository = object : PostRepository {
            override fun savePost(post: Post): Single<Post> =
                Single.error(error)
        }
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        viewModel.setText("test")
        viewModel.addPost()

        val equals = Status.Error(error)
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }

    @Test
    fun `addPost success`() {
        val eventRepository = object : PostRepository {
            override fun savePost(event: Post): Single<Post> =
                Single.fromCallable { TestPost(id = 1L, content = "test").toDomainPost() }
        }
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        viewModel.addPost()

        val equals = Status.Idle
        val result = viewModel.state.value.status

        assertEquals(equals, result)
    }


    @Test
    fun setTextTest() {
        val eventRepository = object : PostRepository {}
        val viewModel = NewPostViewModel(
            postRepository = eventRepository,
            schedulersProvider = TestSchedulersProvider,
        )

        val testText = "test"

        viewModel.setText(testText)

        val equals = testText
        val result = viewModel.state.value.textContent

        assertEquals(equals, result)
    }
}