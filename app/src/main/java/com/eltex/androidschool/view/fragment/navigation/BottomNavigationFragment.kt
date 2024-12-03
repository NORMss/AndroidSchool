package com.eltex.androidschool.view.fragment.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val postsClickListIterator = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomNavigation_to_newPostFragment)
        }

        val eventsClickListIterator = View.OnClickListener {
            findNavController()
                .navigate(R.id.action_bottomNavigation_to_newEventFragment)
        }

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.postFragment -> {
                    binding.save.setOnClickListener(postsClickListIterator)
                    binding.save.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                }

                R.id.eventFragment -> {
                    binding.save.setOnClickListener(eventsClickListIterator)
                    binding.save.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.usersFragment -> {
                    binding.save.setOnClickListener(null)
                    binding.save.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)

        return binding.root
    }
}