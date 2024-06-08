package com.practice.shreebhagavadgita.ui.fragments.settings.savedverses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.databinding.FragmentSavedVersesBinding
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVerses
import com.practice.shreebhagavadgita.viewmodel.MainViewModel

class SavedVersesFragment : Fragment(R.layout.fragment_saved_verses) {

    private lateinit var binding : FragmentSavedVersesBinding
    private val viewModels : MainViewModel by viewModels()
    private lateinit var savedVersesAdapter : SavedVersesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedVersesBinding.bind(view)

        getVerseFromRoom()
    }

    private fun getVerseFromRoom() {
       viewModels.getSavedEnglishVerses().observe(viewLifecycleOwner) {
           binding.apply {
               if(it.isEmpty())
                 mtvNoSVerses.visibility = View.VISIBLE
               else
                 mtvNoSVerses.visibility = View.GONE

               rvSavedVerses.apply {
                   savedVersesAdapter = SavedVersesAdapter(::onVersesClicked)
                   adapter = savedVersesAdapter
                   savedVersesAdapter.asyncDiffUtil.submitList(it)
                   binding.SVShimmer.visibility = View.GONE
               }
           }
       }
    }

    private fun onVersesClicked(savedVerses:SavedVerses){
        val bundle = Bundle()
        bundle.putBoolean("showRoomData", true)
        bundle.putInt("chapterNum", savedVerses.chapter_number)
        bundle.putInt("verseNum", savedVerses.verse_number)
        findNavController().navigate(R.id.action_savedVersesFragment_to_versesDetailFragment, bundle)
    }
}