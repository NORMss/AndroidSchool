package com.eltex.androidschool.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_graph)

        binding.openPost.setOnClickListener {
            navController.navigate(R.id.postFragment)
        }

        binding.openEvent.setOnClickListener {
            navController.navigate(R.id.eventFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}