package com.eltex.androidschool.data.repository

import com.eltex.androidschool.domain.model.Attachment
import com.eltex.androidschool.domain.model.AttachmentType
import com.eltex.androidschool.domain.model.Coordinates
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock

class InMemoryPostRepository : PostRepository {
    private var nextId = 0L
    private var authorId = 1000L

    private val _state = MutableStateFlow(
        listOf(
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Анна Смирнова",
                authorJob = "Android Developer",
                authorAvatar = "https://randomuser.me/api/portraits/women/44.jpg",
                content = "Сегодня расскажу, как я внедрила ViewModel в существующее приложение и ускорила работу с состояниями.",
                published = "2024-11-17T14:00:00Z",
                coordinates = Coordinates(lat = 55.7522, long = 37.6156),
                link = "https://example.com/article-viewmodel",
                mentionedMe = false,
                likedByMe = true,
                attachment = null
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Иван Петров",
                authorJob = "Senior Android Developer",
                authorAvatar = null,
                content = "Jetpack Compose - это будущее UI-разработки. Сегодня поделюсь, как внедрил его в большой проект.",
                published = "2024-11-17T10:00:00Z",
                coordinates = Coordinates(lat = 55.7558, long = 37.6176),
                link = "https://example.com/jetpack-compose-guide",
                mentionedMe = true,
                likedByMe = false,
                attachment = Attachment(
                    url = "https://static1.xdaimages.com/wordpress/wp-content/uploads/2021/02/Jetpack-Compose-Beta.jpg",
                    type = AttachmentType.IMAGE
                )
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Мария Кузнецова",
                authorJob = "Middle Android Developer",
                authorAvatar = "https://randomuser.me/api/portraits/women/33.jpg",
                content = "Вчера удалось оптимизировать работу RecyclerView, уменьшив количество ViewHolder. Делюсь кейсом!",
                published = "2024-11-16T16:00:00Z",
                coordinates = Coordinates(lat = 56.8389, long = 60.6057),
                link = "https://example.com/recyclerview-optimization",
                mentionedMe = false,
                likedByMe = true,
                attachment = null
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Дмитрий Иванов",
                authorJob = "Junior Android Developer",
                authorAvatar = null,
                content = "Создал свой первый проект на Kotlin Multiplatform. Пока сложно, но очень интересно!",
                published = "2024-11-15T12:00:00Z",
                coordinates = Coordinates(lat = 55.7558, long = 37.6176),
                link = "https://example.com/kotlin-multiplatform",
                mentionedMe = false,
                likedByMe = false,
                attachment = Attachment(
                    url = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-17931-Banners-for-1.9.20-release-and-KMP-Stable-annoucement_Twitter-1280x720-1.png",
                    type = AttachmentType.IMAGE,
                )
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Алексей Сидоров",
                authorJob = "Android Lead",
                authorAvatar = "https://randomuser.me/api/portraits/men/22.jpg",
                content = "Обсудим подход MVVM? Я недавно пересмотрел свои взгляды и перешел на Clean Architecture.",
                published = "2024-11-14T08:00:00Z",
                coordinates = Coordinates(lat = 59.9343, long = 30.3351),
                link = "https://example.com/mvvm-clean-architecture",
                mentionedMe = true,
                likedByMe = true,
                attachment = null
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Ольга Морозова",
                authorJob = "Android Developer",
                authorAvatar = null,
                content = "На днях освоила Coroutines. Кто-то использует их в своем проекте? Делитесь опытом!",
                published = "2024-11-12T18:00:00Z",
                coordinates = Coordinates(lat = 51.5074, long = -0.1278),
                link = "https://example.com/coroutines-guide",
                mentionedMe = false,
                likedByMe = false,
                attachment = null
            ),
            Post(
                id = nextId++,
                authorId = authorId++,
                author = "Николай Чернов",
                authorJob = "Senior Android Developer",
                authorAvatar = "https://randomuser.me/api/portraits/men/35.jpg",
                content = "На прошлой неделе провел рефакторинг legacy-кода. Иногда это похоже на раскопки археолога. 😅",
                published = "2024-11-10T20:00:00Z",
                coordinates = Coordinates(lat = 48.8566, long = 2.3522),
                link = "https://example.com/legacy-code-refactoring",
                mentionedMe = false,
                likedByMe = true,
                attachment = null
            )
        )
    )

    override fun getPost(): Flow<List<Post>> =
        _state.asStateFlow()

    override fun likeById(id: Long) {
        _state.update { posts ->
            posts.map {
                if (it.id == id) {
                    it.copy(
                        likedByMe = !it.likedByMe
                    )
                } else {
                    it
                }
            }
        }
    }

    override fun deletePostById(id: Long) {
        _state.update { posts ->
            posts.filter {
                it.id != id
            }
        }
    }

    override fun addPost(textContent: String) {
        _state.update { posts ->
            buildList(capacity = posts.size + 1) {
                add(
                    Post(
                        id = nextId++,
                        authorId = authorId++,
                        author = "Sergey Bezborodov",
                        authorJob = "Junior Android Developer",
                        authorAvatar = "https://avatars.githubusercontent.com/u/47896309?v=4",
                        content = textContent,
                        published = Clock.System.now().toString(),
                        coordinates = Coordinates(
                            lat = 54.9833,
                            long = 82.8964,
                        ),
                        link = "https://github.com/NORMss/",
                        mentionedMe = false,
                        likedByMe = false,
                        attachment = null,
                    )
                )
                addAll(posts)
            }
        }
    }

    override fun editPostById(id: Long, textContent: String) {
        _state.update { posts ->
            posts.map { post ->
                if (post.id == id) {
                    Post(
                        id = id,
                        authorId = post.authorId,
                        author = post.author,
                        authorJob = post.authorJob,
                        authorAvatar = post.authorAvatar,
                        content = textContent,
                        published = post.published,
                        coordinates = post.coordinates,
                        link = post.link,
                        mentionedMe = post.mentionedMe,
                        likedByMe = post.likedByMe,
                        attachment = post.attachment
                    )
                } else {
                    post
                }
            }
        }
    }
}