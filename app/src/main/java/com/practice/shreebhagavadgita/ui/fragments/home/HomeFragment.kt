package com.practice.shreebhagavadgita.ui.fragments.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.connectivity.NetworkManager
import com.practice.shreebhagavadgita.databinding.FragmentHomeBinding
import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.datasource.roomdb.SavedChapters
import com.practice.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding : FragmentHomeBinding
    private val viewModels : MainViewModel by viewModels()
    private lateinit var chapterAdapter : ChaptersAdapter
    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("verse_of_day", Context.MODE_PRIVATE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        checkInternet()
        binding.apply {
            ivSettings.setOnClickListener {
               val homeAction = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
               findNavController().navigate(homeAction)
            }
        }
    }

    private fun getAllChapters() {
        lifecycleScope.launch {
            viewModels.getAllChapters().collect { chaptersList ->
                chapterAdapter = ChaptersAdapter(::onChapterClicked, ::onFavouriteClicked, true,::onFFavClicked, viewModels)
                binding.rvChapters.adapter = chapterAdapter
                chapterAdapter.asyncDiffUtil.submitList(chaptersList)
                binding.homeShimmer.visibility = View.GONE
            }
        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if (it == true) {
                getAllChapters()
                showVerseOfDay()
                binding.apply {
                   rvChapters.visibility = View.VISIBLE
                   homeShimmer.visibility = View.VISIBLE
                   mtvShowVerse.visibility = View.VISIBLE
                   mtvVerseOfDay.visibility = View.VISIBLE
                   mtvNoInternet.visibility = View.GONE
                }
            } else {
                binding.apply {
                    homeShimmer.visibility = View.GONE
                    rvChapters.visibility = View.GONE
                    mtvShowVerse.visibility = View.GONE
                    mtvVerseOfDay.visibility = View.GONE
                    mtvNoInternet.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onChapterClicked(chapterDItems:ChaptersDataItem){
        val bundle = Bundle().apply {
            putInt("chapterNumber", chapterDItems.chapter_number)
            putString("chapterTitle", chapterDItems.name_translated)
            putString("chapterDesc", chapterDItems.chapter_summary)
            putInt("verseCount", chapterDItems.verses_count)
        }
        findNavController().navigate(R.id.action_homeFragment_to_versesFragment, bundle)
    }

    private fun onFavouriteClicked(chapterItems: ChaptersDataItem) {
        lifecycleScope.launch {
           viewModels.putSavedChapterSP(chapterItems.chapter_number.toString(), chapterItems.id)
            viewModels.getAllVerses(chapterItems.chapter_number).collect {
                val verseList = arrayListOf<String>()
                for(currentVerse in  it){
                    for(verses in currentVerse.translations){
                        if(verses.language == "english"){
                            verseList.add(verses.description)
                            break
                        }
                    }
                }

                val savedChapters = SavedChapters(
                  chapter_number = chapterItems.chapter_number,
                  chapter_summary = chapterItems.chapter_summary,
                  chapter_summary_hindi = chapterItems.chapter_summary_hindi,
                  id = chapterItems.id,
                  name = chapterItems.name,
                  name_meaning = chapterItems.name_meaning,
                  name_translated = chapterItems.name_translated,
                  name_transliterated = chapterItems.name_transliterated,
                  slug = chapterItems.slug,
                  verses_count = chapterItems.verses_count,
                  verses = verseList //custom added
                )
                viewModels.insertChapters(savedChapters)
            }
        }
    }

    private fun onFFavClicked(chapterItems: ChaptersDataItem){
      lifecycleScope.launch {
        viewModels.deleteSavedChapterSP(chapterItems.chapter_number.toString())
        viewModels.deleteChapter(chapterItems.id)
      }
    }

    //Assignment2
   /* private fun showVerseOfDay() {
      val chapterNumber = (1..18).random()
      val verseNumber = (1..80).random()
      lifecycleScope.launch {
        viewModels.getAParticularVerse(chapterNumber, verseNumber).collect {
          for (i in it.translations) {
            if(i.language == "english"){
              binding.apply {
                mtvVerseOfDay.text = i.description
              }
              break
            }
          }
        }
      }
    }*/

    private fun showVerseOfDay() {
        val currentTimeMillis = System.currentTimeMillis()
        val lastShownTimeMillis = sharedPreferences.getLong("last_shown_time", 0)

        // Check if 24 hours have passed since the last time the verse was shown
        if (currentTimeMillis - lastShownTimeMillis >= 24 * 60 * 60 * 1000 || lastShownTimeMillis == 0L) {
            // Generate random chapter and verse numbers
            val chapterNumber = (1..18).random()
            val verseNumber = (1..80).random()

            // Fetch the verse
            lifecycleScope.launch {
                viewModels.getAParticularVerse(chapterNumber, verseNumber).collect { verse ->
                    // Display the verse
                    val englishTranslation = verse.translations.find { it.language == "english" }
                    val verseText = englishTranslation?.description ?: ""
                    binding.apply {
                        mtvVerseOfDay.text = verseText
                    }
                    // Store the verse text and current time as the last shown verse and time
                    sharedPreferences.edit {
                        putString("last_shown_verse", verseText)
                        putLong("last_shown_time", currentTimeMillis)
                    }
                }
            }
        } else {
            // If 24 hours haven't passed, retrieve the last shown verse from SharedPreferences
            val lastShownVerse = sharedPreferences.getString("last_shown_verse", "")
            if (!lastShownVerse.isNullOrEmpty()) {
                binding.mtvVerseOfDay.text = lastShownVerse
            }
        }
    }
}