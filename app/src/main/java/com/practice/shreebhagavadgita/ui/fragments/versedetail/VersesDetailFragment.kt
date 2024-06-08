package com.practice.shreebhagavadgita.ui.fragments.versedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.connectivity.NetworkManager
import com.practice.shreebhagavadgita.databinding.FragmentVersesDetailBinding
import com.practice.shreebhagavadgita.datamodels.versesdata.Commentary
import com.practice.shreebhagavadgita.datamodels.versesdata.Translation
import com.practice.shreebhagavadgita.datamodels.versesdata.VersesDataItem
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVerses
import com.practice.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class VersesDetailFragment : Fragment(R.layout.fragment_verses_detail) {

    private var binding : FragmentVersesDetailBinding? = null
    private val viewModels : MainViewModel by viewModels()
    private var chapterNum = 0
    private var verseNum = 0
    private var verseDetail = MutableLiveData<VersesDataItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVersesDetailBinding.bind(view)

        receiveChapterVerseNumber()
        onSavedVerse()
        onReadMore()
        getData()
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if(it == true){
                binding?.apply {
                    mtvNoInternet.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                getVerseDetail()
            }else {
                binding?.apply {
                    mtvNoInternet.visibility = View.VISIBLE
                    mtvVDNo.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    mtvVDText.visibility = View.GONE
                    mtvVDTransliterationIfEng.visibility = View.GONE
                    mtvVDWordIfEng.visibility = View.GONE
                    mtvVDTranslation.visibility = View.GONE
                    mcvTranslation.visibility = View.GONE
                    fabTLeft.visibility = View.GONE
                    fabTRight.visibility = View.GONE
                    mtvVDCommentary.visibility = View.GONE
                    mcvCommentary.visibility = View.GONE
                    fabCLeft.visibility = View.GONE
                    fabCRight.visibility = View.GONE
                }
            }
        }
    }

    private fun onReadMore() {
        var isExpanded = false
        binding?.apply {
            mtvReadMore.setOnClickListener {
                if(!isExpanded){
                    isExpanded = true
                    mtvCAuthorDesc.maxLines = 100
                } else {
                    isExpanded = false
                    mtvCAuthorDesc.maxLines = 4
                }
            }
        }
    }

    private fun getVerseDetail() {
       lifecycleScope.launch {
           viewModels.getAParticularVerse(chapterNum, verseNum).collect { verse ->
              verseDetail.postValue(verse)
              binding?.apply {

                 mtvVDText.text = verse.text
                 mtvVDTransliterationIfEng.text = verse.transliteration
                 mtvVDWordIfEng.text = verse.word_meanings
                  //for translation
                 val engTranslationList = arrayListOf<Translation>()
                 for(i in verse.translations){
                    if(i.language == "english"){
                       engTranslationList.add(i)
                    }
                 }
                 val engTranslationListSize = engTranslationList.size
                 if(engTranslationList.isNotEmpty()) {
                     binding.apply {
                         mtvVDAuthorName.text = engTranslationList[0].author_name
                         mtvVDAuthorDesc.text = engTranslationList[0].description
                         if(engTranslationListSize == 1) binding?.fabTRight?.visibility = View.GONE
                         var i = 0
                         binding?.fabTRight?.setOnClickListener {
                            if(i < engTranslationListSize - 1){
                               i++
                                mtvVDAuthorName.text = engTranslationList[i].author_name
                                mtvVDAuthorDesc.text = engTranslationList[i].description
                                binding?.fabTLeft?.visibility = View.VISIBLE

                                if(i == engTranslationListSize - 1)  binding!!.fabTRight.visibility = View.GONE
                            }
                         }
                         binding?.fabTLeft?.setOnClickListener {
                            if(i > 0){
                               i--
                               mtvVDAuthorName.text = engTranslationList[i].author_name
                               mtvVDAuthorDesc.text = engTranslationList[i].description
                               binding?.fabTRight?.visibility = View.VISIBLE
                               if(i == 0) binding!!.fabTLeft.visibility = View.GONE
                            }
                         }
                     }
                 }

                 //for commentary
                 val engCommentaryList = arrayListOf<Commentary>()
                 for(i in verse.commentaries){
                     if(i.language == "hindi"){
                         engCommentaryList.add(i)
                     }
                 }
                 val engCommentaryListSize = engCommentaryList.size
                 if(engCommentaryList.isNotEmpty()){
                    binding?.apply {
                       mtvCAuthorName.text = engCommentaryList[0].author_name
                       mtvCAuthorDesc.text = engCommentaryList[0].description
                       if(engCommentaryListSize == 1) binding?.fabCRight?.visibility = View.GONE
                        var i = 0
                        binding?.fabCRight?.setOnClickListener {
                            if(i < engCommentaryListSize - 1){
                                i++
                                mtvCAuthorName.text = engCommentaryList[i].author_name
                                mtvCAuthorDesc.text = engCommentaryList[i].description
                                binding?.fabCLeft?.visibility = View.VISIBLE

                                if(i == engCommentaryListSize - 1)  binding!!.fabCRight.visibility = View.GONE
                            }
                        }
                        binding?.fabCLeft?.setOnClickListener {
                            if(i > 0){
                                i--
                                mtvVDAuthorName.text = engCommentaryList[i].author_name
                                mtvVDAuthorDesc.text = engCommentaryList[i].description
                                binding?.fabCRight?.visibility = View.VISIBLE
                                if(i == 0) binding!!.fabCLeft.visibility = View.GONE
                            }
                        }
                    }
                 }
             }
           }

          binding?.apply {
             progressBar.visibility = View.GONE
             mtvVDNo.visibility = View.VISIBLE
             mtvVDText.visibility = View.VISIBLE
             mtvVDTransliterationIfEng.visibility = View.VISIBLE
             mtvVDWordIfEng.visibility = View.VISIBLE
             mtvVDTranslation.visibility = View.VISIBLE
             mcvTranslation.visibility = View.VISIBLE
             mtvVDCommentary.visibility = View.VISIBLE
             mcvCommentary.visibility = View.VISIBLE
             fabCLeft.visibility = View.VISIBLE
             fabCRight.visibility = View.VISIBLE
          }
       }
    }

    private fun receiveChapterVerseNumber() {
       val bundle = arguments
       chapterNum = bundle?.getInt("chapterNum", chapterNum)!!
       verseNum = bundle.getInt("verseNum", verseNum)
        binding?.apply {
           mtvVDNo.text = String.format("|| $chapterNum.$verseNum ||")
        }
    }

    private fun onSavedVerse() {
        binding?.apply {
            val isFavorite = viewModels.getAllSavedVersesKeySP().contains(chapterNum.toString().plus("_${verseNum}"))
            //println("SharedPref Check Get ${chapterNum},${verseNum},${isFavorite}")
            if (isFavorite) {
                ivFavourite.visibility = View.GONE
                ivFillFavourite.visibility = View.VISIBLE
            } else {
                ivFavourite.visibility = View.VISIBLE
                ivFillFavourite.visibility = View.GONE
            }

            ivFillFavourite.setOnClickListener {
                ivFavourite.visibility = View.VISIBLE
                ivFillFavourite.visibility = View.GONE
                deleteVerses()
            }

           ivFavourite.setOnClickListener {
             ivFavourite.visibility = View.GONE
             ivFillFavourite.visibility = View.VISIBLE
             savingVerseDetail()
          }
       }
    }

    private fun savingVerseDetail(){
        verseDetail.observe(viewLifecycleOwner) {
           //for translation
           val engTranslationList = arrayListOf<Translation>()
           for(i in it.translations){
               if(i.language == "english"){
                   engTranslationList.add(i)
               }
           }
           //for commentary
           val engCommentaryList = arrayListOf<Commentary>()
           for(i in it.commentaries){
             if(i.language == "hindi"){
                   engCommentaryList.add(i)
               }
           }
           val savedVerses = SavedVerses(it.chapter_number, engCommentaryList, it.id, it.slug, it.text,
              engTranslationList, it.transliteration, it.verse_number, it.word_meanings)
             lifecycleScope.launch {
              viewModels.putSavedVersesSP(savedVerses.chapter_number.toString().plus("_${savedVerses.verse_number}"), 1)
              //println("SharedPref Check Put ${savedVerses.chapter_number},${savedVerses.verse_number}")
              viewModels.insertEnglishVerses(savedVerses)
          }
       }
    }

    private fun deleteVerses(){
      lifecycleScope.launch {
        viewModels.deleteSavedVersesSP(chapterNum.toString().plus("_${verseNum}"))
        viewModels.deleteAParticularVerse(chapterNum, verseNum)
      }
    }

    private fun getData() {
      val bundle = arguments
      val showRoomData = bundle?.getBoolean("showRoomData", false)
       if(showRoomData == true){
          binding?.apply {
             ivFavourite.visibility = View.INVISIBLE
             ivFillFavourite.visibility = View.INVISIBLE
          }
           viewModels.getParticularVerses(chapterNum, verseNum)
               .observe(viewLifecycleOwner) { verse ->
                   binding?.apply {
                       mtvVDText.text = verse.text
                       mtvVDTransliterationIfEng.text = verse.transliteration
                       mtvVDWordIfEng.text = verse.word_meanings
                       //for translation
                       val engTranslationList = arrayListOf<Translation>()
                       for (i in verse.translations) {
                           if (i.language == "english") {
                               engTranslationList.add(i)
                           }
                       }
                       val engTranslationListSize = engTranslationList.size
                       if (engTranslationList.isNotEmpty()) {
                           binding.apply {
                               mtvVDAuthorName.text = engTranslationList[0].author_name
                               mtvVDAuthorDesc.text = engTranslationList[0].description
                               if (engTranslationListSize == 1) fabTRight.visibility = View.GONE
                               var i = 0
                               fabTRight.setOnClickListener {
                                   if (i < engTranslationListSize - 1) {
                                       i++
                                       mtvVDAuthorName.text = engTranslationList[i].author_name
                                       mtvVDAuthorDesc.text = engTranslationList[i].description
                                       binding?.fabTLeft?.visibility = View.VISIBLE

                                       if (i == engTranslationListSize - 1) binding!!.fabTRight.visibility =
                                           View.GONE
                                   }
                               }
                               fabTLeft.setOnClickListener {
                                   if (i > 0) {
                                       i--
                                       mtvVDAuthorName.text = engTranslationList[i].author_name
                                       mtvVDAuthorDesc.text = engTranslationList[i].description
                                       binding?.fabTRight?.visibility = View.VISIBLE
                                       if (i == 0) binding!!.fabTLeft.visibility = View.GONE
                                   }
                               }
                           }
                       }

                       //for commentary
                       val engCommentaryList = arrayListOf<Commentary>()
                       for (i in verse.commentaries) {
                           if (i.language == "hindi") {
                               engCommentaryList.add(i)
                           }
                       }
                       val engCommentaryListSize = engCommentaryList.size
                       if (engCommentaryList.isNotEmpty()) {
                           binding?.apply {
                               mtvCAuthorName.text = engCommentaryList[0].author_name
                               mtvCAuthorDesc.text = engCommentaryList[0].description
                               if (engCommentaryListSize == 1) fabCRight.visibility = View.GONE
                               var i = 0
                               fabCRight.setOnClickListener {
                                   if (i < engCommentaryListSize - 1) {
                                       i++
                                       mtvCAuthorName.text = engCommentaryList[i].author_name
                                       mtvCAuthorDesc.text = engCommentaryList[i].description
                                       fabCLeft.visibility = View.VISIBLE

                                       if (i == engCommentaryListSize - 1) fabCRight.visibility =
                                           View.GONE
                                   }
                               }
                               fabCLeft.setOnClickListener {
                                   if (i > 0) {
                                       i--
                                       mtvVDAuthorName.text = engCommentaryList[i].author_name
                                       mtvVDAuthorDesc.text = engCommentaryList[i].description
                                       fabCRight.visibility = View.VISIBLE
                                       if (i == 0) fabCLeft.visibility = View.GONE
                                   }
                               }
                           }
                       }
                   }
                   binding?.apply {
                       progressBar.visibility = View.GONE
                       mtvVDNo.visibility = View.VISIBLE
                       mtvVDText.visibility = View.VISIBLE
                       mtvVDTransliterationIfEng.visibility = View.VISIBLE
                       mtvVDWordIfEng.visibility = View.VISIBLE
                       mtvVDTranslation.visibility = View.VISIBLE
                       mcvTranslation.visibility = View.VISIBLE
                       mtvVDCommentary.visibility = View.VISIBLE
                       mcvCommentary.visibility = View.VISIBLE
                       fabCLeft.visibility = View.VISIBLE
                       fabCRight.visibility = View.VISIBLE
                   }
               }
       }
       else {
         checkInternet()
       }
    }
}