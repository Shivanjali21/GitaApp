package com.practice.shreebhagavadgita.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var binding : FragmentSettingsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding!!.apply {
           clSavedChapters.setOnClickListener {
             val settingAction = SettingsFragmentDirections.actionSettingsFragmentToSavedChaptersFragment()
             findNavController().navigate(settingAction)
           }
           clSavedVerses.setOnClickListener {
             val settingAction = SettingsFragmentDirections.actionSettingsFragmentToSavedVersesFragment()
             findNavController().navigate(settingAction)
           }
        }
    }
}