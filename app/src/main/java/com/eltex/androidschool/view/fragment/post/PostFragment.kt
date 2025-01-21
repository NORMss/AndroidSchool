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
import androidx.recyclerview.widget.RecyclerView
import com.eltex.androidschool.App
import com.eltex.androidschool.R
import com.eltex.androidschool.data.repository.RemotePostRepository
import com.eltex.androidschool.databinding.FragmentPostBinding
import com.eltex.androidschool.utils.remote.getErrorText
import com.eltex.androidschool.view.common.OffsetDecoration
import com.eltex.androidschool.view.fragment.editpost.EditPostFragment
import com.eltex.androidschool.view.fragment.newpost.NewPostFragment
import com.eltex.androidschool.view.fragment.post.adapter.grouped.PostGroupedAdapter
import com.eltex.androidschool.view.fragment.post.adapter.post.PostAdapter
import com.eltex.androidschool.view.fragment.post.effecthendler.PostEffectHandler
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer
import com.eltex.androidschool.view.fragment.post.reducer.PostReducer.Companion.PAGE_SIZE
import com.eltex.androidschool.view.mapper.PostGroupedMapper
import com.eltex.androidschool.view.mapper.PostUiMapper
import com.eltex.androidschool.view.model.PostUi
import com.eltex.androidschool.view.util.toast.toast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.getValue

class PostFragment : Fragment() {
    private val adapter = PostGroupedAdapter(
        postListener = object : PostAdapter.PostListener {
            override fun onLikeClicked(post: PostUi) {
                viewModel.accept(PostMessage.Like(post))
            }

            override fun onShareClicked(post: PostUi) {
                share(post)
            }

            override fun onMoreClicked(post: PostUi, view: View) {
                popupMenuLogic(post, view)
            }
        }

    )

    private val mapper = PostGroupedMapper()

    private val viewModel by viewModels<PostViewModel> {
        viewModelFactory {
            addInitializer(PostViewModel::class) {
                PostViewModel(
                    store = PostStore(
                        reducer = PostReducer(
                            mapper = PostUiMapper(),
                        ),
                        effectHandler = PostEffectHandler(
                            repository = RemotePostRepository(
                                postApi = (context?.applicationContext as App).postApi,
                            ),
                        ),
                        initMessages = setOf(
                            PostMessage.Refresh
                        ),
                        initState = PostState(),
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
        val binding = FragmentPostBinding.inflate(inflater, container, false)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            NewPostFragment.POST_SAVED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(PostMessage.Refresh)
        }

        requireActivity().supportFragmentManager.setFragmentResultListener(
            EditPostFragment.POST_EDITED,
            viewLifecycleOwner
        ) { _, _ ->
            viewModel.accept(PostMessage.Refresh)
        }

        binding.posts.adapter = adapter

        binding.posts.addItemDecoration(
            OffsetDecoration(
                horizontalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
                verticalOffset = resources.getDimensionPixelSize(R.dimen.list_offset),
            )
        )

        binding.retryButton.setOnClickListener {
            viewModel.accept(PostMessage.Refresh)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.accept(PostMessage.Refresh)
        }

        binding.posts.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    val itemCount = adapter.itemCount
                    val adapterPosition = binding.posts.getChildAdapterPosition(view)
                    if (itemCount - PAGE_SIZE / 2 == adapterPosition) {
                        viewModel.accept(PostMessage.LoadNextPage)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) = Unit
            }
        )


        observeViewModelState(binding)

        return binding.root
    }

    private fun observeViewModelState(binding: FragmentPostBinding) {
        viewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { state ->
                binding.errorGroup.isVisible = state.isEmptyError
                val errorText = state.emptyError?.getErrorText(requireContext())
                binding.errorText.text = errorText
                binding.progress.isVisible = state.isEmptyLoading
                binding.swipeRefresh.isRefreshing = state.isRefreshing
                binding.swipeRefresh.isVisible = state.posts.isNotEmpty()
                if (state.singleError != null) {
                    val singleErrorText = state.singleError.getErrorText(requireContext())
                    Toast.makeText(
                        requireContext(),
                        singleErrorText,
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.accept(PostMessage.HandleError)
                }
                adapter.submitList(mapper.map(state.posts))
                binding.root.visibility = View.VISIBLE
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun popupMenuLogic(post: PostUi, view: View) {
        PopupMenu(view.context, view).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete -> {
                        viewModel.accept(PostMessage.Delete(post))
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

    private fun share(post: PostUi) {
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
