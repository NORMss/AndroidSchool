package com.eltex.androidschool.data.repository

import com.eltex.androidschool.data.remote.api.PostApi
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository

/**
 * [RemotePostRepository] is an implementation of [PostRepository] that fetches post data from a remote source
 * via the [PostApi].
 *
 * This class handles interactions with the network layer to retrieve, save, like/unlike, and delete posts.
 *
 * @property postApi The API service used to make network requests related to posts.
 */
class RemotePostRepository(
    private val postApi: PostApi
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        return postApi.getPosts()
    }

    /**
     * Retrieves a list of posts that are newer than the specified post ID.
     *
     * This function fetches posts from the remote API that have an ID greater than the given [id].
     * The result is a list of [Post] objects, ordered from newest to oldest, relative to the provided ID.
     *
     * @param id The ID of the post to compare against. Posts with IDs greater than this value will be returned.
     * @return A list of [Post] objects that are newer than the specified post ID.
     * @throws Exception If there is an error during the network request or data processing.
     */
    override suspend fun getPostsNewer(id: Long): List<Post> {
        return postApi.getPostsNewer(id)
    }

    /**
     * Retrieves a list of posts that were created before a specified post ID.
     *
     * This function fetches a specified number of posts from the API that precede the post identified by the given ID.
     * The returned list is ordered from the most recent to the oldest post, where all posts in the list have an ID lower than the provided `id`.
     *
     * @param id The ID of the post serving as the upper bound (exclusive) for retrieving older posts.
     *           Posts with IDs greater than or equal to this `id` will not be included in the result.
     * @param count The maximum number of posts to retrieve.
     * @return A list of [Post] objects, ordered from the most recent to the oldest, that were created before the post with the given `id`.
     *         Returns an empty list if no posts are found that meet the criteria.
     * @throws Exception If an error occurs during the API request. Specific exception types might vary depending on the underlying API implementation.
     */
    override suspend fun getPostsBefore(
        id: Long,
        count: Int
    ): List<Post> {
        return postApi.getPostsBefore(id, count)
    }

    /**
     * Retrieves a list of posts that come after a specific post ID.
     *
     * This function fetches a specified number of posts from the data source,
     * starting after the post identified by the provided `id`.
     *
     * @param id The ID of the post to start fetching after. Posts with an ID greater than this will be included.
     * @param count The maximum number of posts to retrieve.
     * @return A list of [Post] objects. The list may be empty if no posts are found after the specified ID
     *         or if the `count` is 0 or negative.
     * @throws Exception if there is an error during the network request or data processing. The specific type
     *         of exception depends on the underlying implementation of the `postApi`.
     */
    override suspend fun getPostsAfter(
        id: Long,
        count: Int
    ): List<Post> {
        return postApi.getPostsAfter(id, count)
    }

    /**
     * Retrieves the latest posts from the remote data source.
     *
     * This function fetches a specified number of the most recently created posts.
     *
     * @param count The number of latest posts to retrieve. Must be a positive integer.
     * @return A [List] of [Post] objects representing the latest posts.
     * @throws Exception if there is an error fetching the posts from the API.
     * @throws IllegalArgumentException if the provided `count` is not a positive integer.
     */
    override suspend fun getPostsLatest(count: Int): List<Post> {
        return postApi.getPostsLatest(count)
    }

    /**
     * Likes or unlikes a post by its ID.
     *
     * This function interacts with the remote Post API to either like or unlike a post
     * based on the provided `isLiked` flag.
     *
     * @param id The ID of the post to like or unlike.
     * @param isLiked A boolean flag indicating the desired action:
     *                - `true`: Unlikes the post (removes the like).
     *                - `false`: Likes the post (adds a like).
     * @return The updated [Post] object after the like/unlike action has been performed.
     * @throws Exception if there's an issue interacting with the API (e.g., network error, server error).
     */
    override suspend fun likeById(
        id: Long,
        isLiked: Boolean
    ): Post {
        return when (isLiked) {
            true -> postApi.unlikeById(id)
            false -> postApi.likeById(id)
        }
    }

    /**
     * Saves a post to the remote data source.
     *
     * This function interacts with the `postApi` to persist a given [Post] object.
     * It's a suspending function, meaning it can be called from a coroutine and
     * will pause execution until the network operation is complete.
     *
     * @param post The [Post] object to be saved.
     * @return The saved [Post] object, potentially with updated information
     *         like a server-generated ID or timestamp.
     * @throws Exception if any error occurs during the network operation. (This is implicit due to the nature of network calls and the use of `postApi`)
     */
    override suspend fun savePost(post: Post): Post {
        return postApi.save(post)
    }

    /**
     * Deletes a post by its ID.
     *
     * This function sends a request to the API to delete a post identified by the given [id].
     * The operation is performed asynchronously and suspends until the deletion is complete or fails.
     *
     * @param id The ID of the post to be deleted.
     * @throws Exception if there is an error during the deletion process, such as network issues or server errors.
     */
    override suspend fun deleteById(id: Long) {
        return postApi.deleteById(id)
    }
}