package com.eltex.androidschool.view.fragment.post

import com.eltex.androidschool.domain.mapper.GroupByDateMapper
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.model.TestPost
import com.eltex.androidschool.repository.TestErrorPostRepository
import com.eltex.androidschool.view.common.Status
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.datetime.DateSeparators
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PostViewModelTest {
    @Test
    fun `likeById error then state contains error`() {
        val error = RuntimeException("Like failed")
        val postRepository = object : TestErrorPostRepository {
            override suspend fun likeById(id: Long, isLiked: Boolean): Post =
                throw error
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `deleteById error then state contains error`() {
        val error = RuntimeException("Delete failed")
        val postRepository = object : TestErrorPostRepository {
            override suspend fun deleteById(id: Long) =
                throw error
        }
        val mapper = object : GroupByDateMapper<Post, PostUi> {
            override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                emptyList()
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = mapper,
        )

        viewModel.deletePost(1L)

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `loadPosts error then state contains error`() {
        val error = RuntimeException("get posts failed")
        val postRepository = object : TestErrorPostRepository {
            override suspend fun getPosts(): List<Post> =
                throw error
        }
        val mapper = object : GroupByDateMapper<Post, PostUi> {
            override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                emptyList()
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = mapper,
        )

        viewModel.loadPosts()

        val result = viewModel.state.value.status
        val equals = Status.Error(error)

        assertEquals(result, equals)
    }

    @Test
    fun `likeById success`() {
        val postRepository = object : TestErrorPostRepository {
            override suspend fun getPosts(): List<Post> =
                listOf(
                    TestPost(1).toDomainPost(),
                    TestPost(2).toDomainPost()
                )

            override suspend fun likeById(id: Long, isLiked: Boolean): Post =
                TestPost(1).toDomainPost()
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }

    @Test
    fun `deleteById success`() {
        val postRepository = object : TestErrorPostRepository {}
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
        )
        viewModel.deletePost(1L)

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }

    @Test
    fun `loadPosts success`() {
        val postRepository = object : TestErrorPostRepository {
            override suspend fun getPosts(): List<Post> =
                listOf(TestPost(1L).toDomainPost())
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
        )
        viewModel.loadPosts()

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }

    @Test
    fun `consumerError clear status`() {
        val error = RuntimeException("Like failed")
        val postRepository = object : TestErrorPostRepository {
            override suspend fun likeById(id: Long, isLiked: Boolean): Post =
                throw error
        }
        val viewModel = PostViewModel(
            postRepository = postRepository,
            mapper = object : GroupByDateMapper<Post, PostUi> {
                override fun map(from: List<Post>): List<DateSeparators.GroupByDate<PostUi>> =
                    emptyList()
            },
        )
        viewModel.likeById(1L, true)

        val resultWithError = viewModel.state.value.status
        val equalsWithError = Status.Error(error)

        assertEquals(resultWithError, equalsWithError)

        viewModel.consumerError()

        val result = viewModel.state.value.status
        val equals = Status.Idle

        assertEquals(result, equals)
    }
}