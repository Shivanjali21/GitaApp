package com.practice.shreebhagavadgita.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var binding : FragmentSplashBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)

        binding!!.apply {
           Handler(Looper.getMainLooper()).postDelayed({
             val splashAction = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
             findNavController().navigate(splashAction)
           },3000)
        }
    }
}