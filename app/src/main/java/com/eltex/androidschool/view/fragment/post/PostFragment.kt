package com.eltex.androidschool.view.fragment.post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.view.activity.post.EditPostActivity
import com.eltex.androidschool.view.activity.post.NewPostActivity
import com.eltex.androidschool.data.local.DataStoreHolder
import com.eltex.androidschool.data.local.LocalPostsManagerImpl
import com.eltex.androidschool.data.repository.LocalPostRepository
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.ui.ObserveAsEvents
import com.eltex.androidschool.ui.OffsetDecoration
import com.eltex.androidschool.utils.constants.DataStoreConfig.POST_CONFIG
import com.eltex.androidschool.utils.constants.IntentPutExtra
import com.eltex.androidschool.utils.resourcemanager.AndroidResourceManager
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.adapter.postbydate.PostByDateAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostFragment : Fragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val adapter = PostByDateAdapter(
        object : PostAdapter.PostListener {
            override fun onLikeClicked(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShareClicked(post: Post) {
                share(post)
            }

            override fun onMoreClicked(post: Post, view: View) {
                popupMenuLogic(post, view)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        binding.postsByDate.posts.adapter = adapter

        binding.newPost.setOnClickListener {
            val intent = Intent(requireContext(), NewPostActivity::class.java)
            newPostLauncher.launch(intent)
        }

        binding.postsByDate.posts.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
                verticalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
            )
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModelState() {
        viewModel.state
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                adapter.submitList(state.postsByDate)
                binding.root.visibility = View.VISIBLE
                state.toast?.let { toastData ->
                    ObserveAsEvents(toast = toastData, activity = activity)
                }
            }
            .launchIn(lifecycleScope)
    }

    private val newPostLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val content = result.data?.getStringArrayListExtra(Intent.EXTRA_TEXT)
                content?.let { viewModel.addPost(it[0], it[1]) }
            }
        }

    private val editPostLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data?.getStringExtra(Intent.EXTRA_TEXT)
                data?.let {
                    val post = Json.decodeFromString<Post>(data)
                    println(post)
                    viewModel.editPost(post.id, post.content)
                }
            }
        }

    private fun popupMenuLogic(post: Post, view: View) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        viewModel.deletePost(post.id)
                        true
                    }

                    R.id.edit -> {
                        val intent = Intent(requireContext(), EditPostActivity::class.java).apply {
                            putExtra(IntentPutExtra.KEY_POST, Json.encodeToString(post))
                        }
                        editPostLauncher.launch(intent)
                        true
                    }

                    else -> false
                }
            }
            show()
        }
    }

    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    resourceManager = AndroidResourceManager(
                        requireContext().applicationContext,
                    ),
                    postRepository = LocalPostRepository(
                        LocalPostsManagerImpl(
                            requireContext().applicationContext,
                            DataStoreHolder.getInstance(
                                requireContext().applicationContext,
                                POST_CONFIG
                            ),
                        ),
                    ),
                )
            }
        }
    }

    private fun share(post: Post) {
        val intent = Intent.createChooser(
            Intent(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, post.content)
                .setType("text/plain"),
            null,
        )

        runCatching { startActivity(intent) }
            .onFailure {
                activity?.toast(R.string.app_not_found, false)
            }
    }
}