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

        val navController =
            requireNotNull(childFragmentManager.findFragmentById(R.id.container)).findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        return binding.root
    }
}