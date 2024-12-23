package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.TestSchedulersProvider
import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import io.reactivex.rxjava3.core.Single
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PostViewModelTest {
    @Test
    fun `likeById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val postRepository = object : PostRepository {
            override fun likeById(id: Long, isLiked: Boolean): Single<Post> =
                Single.error(error)
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
            schedulersProvider = TestSchedulersProvider
        )

        viewModel.likeById(1L, true)

        assertEquals(error, viewModel.state.value.status.throwableOtNull)
    }
}