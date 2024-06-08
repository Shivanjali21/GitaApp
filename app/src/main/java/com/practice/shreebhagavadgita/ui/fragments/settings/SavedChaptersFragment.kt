package com.practice.shreebhagavadgita.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.databinding.FragmentSavedChaptersBinding
import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.ui.fragments.home.ChaptersAdapter
import com.practice.shreebhagavadgita.viewmodel.MainViewModel

class SavedChaptersFragment : Fragment(R.layout.fragment_saved_chapters) {

    private var binding : FragmentSavedChaptersBinding? = null
    private val viewModels : MainViewModel by viewModels()
    private val chapterAdapter : ChaptersAdapter by lazy {
      ChaptersAdapter(
          ::onChapterItemClicked,
          ::favouriteClicked,
          false,
          ::onFFavClicked,
          viewModels
      )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedChaptersBinding.bind(view)

        getSavedChapters()
    }

    private fun getSavedChapters() {
        viewModels.getSavedChapters().observe(viewLifecycleOwner) {
            val chaptersList = arrayListOf<ChaptersDataItem>()
            for (i in it) {
                val chapterItem = ChaptersDataItem(
                    i.chapter_number,
                    i.chapter_summary,
                    i.chapter_summary_hindi,
                    i.id,
                    i.name,
                    i.name_meaning,
                    i.name_translated,
                    i.name_transliterated,
                    i.slug,
                    i.verses_count
                )
                chaptersList.add(chapterItem)
            }
            if (chaptersList.isEmpty()) {
                binding?.apply {
                    savedChaptersShimmer.visibility = View.GONE
                    rvSavedChapters.visibility = View.GONE
                    mtvNoSChapters.visibility = View.VISIBLE
                }
            }
            binding?.apply {
               rvSavedChapters.apply {
                  adapter = chapterAdapter
                  chapterAdapter.asyncDiffUtil.submitList(chaptersList)
               }
               savedChaptersShimmer.visibility = View.GONE
               rvSavedChapters.visibility = View.VISIBLE
            }
        }
    }

    private fun onChapterItemClicked(chapterItem:ChaptersDataItem) {
       val bundle = Bundle().apply {
          putInt("chapterNumber", chapterItem.chapter_number)
          putBoolean("showRoomData", true)
       }
       findNavController().navigate(R.id.action_savedChaptersFragment_to_versesFragment, bundle)
    }

    private fun favouriteClicked(chapterItem: ChaptersDataItem) {}

    private fun onFFavClicked(chapterItem: ChaptersDataItem) {}
}