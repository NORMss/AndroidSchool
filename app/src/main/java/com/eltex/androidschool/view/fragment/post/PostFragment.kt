package com.eltex.androidschool.view.fragment.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.domain.model.Post
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.utils.toast.toast
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.editpost.EditPostFragment
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.adapter.postbydate.PostByDateAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

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

    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    postRepository = RemotePostRepository(
                        (context?.applicationContext as App).client
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)

        _binding?.postsByDate?.posts?.adapter = adapter

        binding.postsByDate.posts.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
                verticalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
            )
        )

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

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
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.errorGroup.isVisible = state.isEmptyError
                state.status.throwableOtNull?.getErrorText(requireContext())?.let { errorText ->
                    binding.errorText.text = errorText
                    binding.progress.isVisible = state.isEmptyLoading

                    if (state.isRefreshError) {
                        Toast.makeText(
                            requireContext(),
                            errorText,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.consumerError()
                    }
                }
                adapter.submitList(state.postsByDate)
                binding.root.visibility = View.VISIBLE
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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
                        requireParentFragment().requireParentFragment().findNavController()
                            .navigate(
                                R.id.action_bottomNavigation_to_editPostFragment,
                                bundleOf(
                                    EditPostFragment.POST_ID to post.id,
                                )
                            )
                        true
                    }

                    else -> false
                }
            }
            show()
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
