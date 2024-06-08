package com.practice.shreebhagavadgita.ui.fragments.verses

import android.os.Bundle
import android.speech.tts.UtteranceProgressListener
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practice.shreebhagavadgita.R
import com.practice.shreebhagavadgita.connectivity.NetworkManager
import com.practice.shreebhagavadgita.databinding.FragmentVersesBinding
import com.practice.shreebhagavadgita.utils.TextToSpeechManager
import com.practice.shreebhagavadgita.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class VersesFragment : Fragment(R.layout.fragment_verses) {

    private var binding : FragmentVersesBinding? = null
    private val viewModels : MainViewModel by viewModels()
    private lateinit var versesAdapter : VersesAdapter
    private var chapterNo = 0
    private lateinit var textToSpeechManager: TextToSpeechManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVersesBinding.bind(view)

        textToSpeechManager = TextToSpeechManager(requireActivity())
        onPlayClick()
        onPauseClick()
        setUtteranceProgressListener()

        receiveData()
        onReadMore()
        getData()
    }

    private fun onPlayClick() {
        binding?.apply {
            ivPlay.setOnClickListener {
               proBar.visibility = View.VISIBLE
               ivPlay.visibility = View.GONE
               textToSpeechManager.speak(mtvVersesCDesc.text.toString())
            }
        }
    }

    private fun onPauseClick() {
        binding?.apply {
           ivPause.setOnClickListener {
              ivPause.visibility = View.GONE
              ivPlay.visibility = View.VISIBLE
              textToSpeechManager.stop(false)
           }
        }
    }

    private fun setUtteranceProgressListener() {
       textToSpeechManager.setUtteranceProgressListener(object : UtteranceProgressListener(){
          override fun onStart(utteranceId: String?) {
            activity?.runOnUiThread {
               binding?.proBar?.visibility = View.GONE
               binding?.ivPause?.visibility = View.VISIBLE
            }
          }

          override fun onDone(utteranceId: String?) {
              activity?.runOnUiThread {
                 binding?.ivPause?.visibility = View.GONE
                 binding?.ivPlay?.visibility = View.GONE
                 Toast.makeText(context, "Completed!", Toast.LENGTH_SHORT).show()
              }
          }

          override fun onError(utteranceId: String?) {
            binding?.proBar?.visibility = View.GONE
          }
       })
    }

    private fun getData() {
      val bundle = arguments
      val showRoomData = bundle?.getBoolean("showRoomData", false)
        if (showRoomData == true) {
            getDataFromRoom()
        } else {
            checkInternet()
        }
    }

    private fun getDataFromRoom() {
       viewModels.getAParticularChapter(chapterNo).observe(viewLifecycleOwner) {
           binding?.apply {
               mtvVersesChapter.text = String.format("Chapter ${it.chapter_number}")
               mtvVersesCName.text = it.name_translated
               mtvVersesCDesc.text = it.chapter_summary
               mtvNoVerses.text = String.format(" ${it.verses_count} Verses")
           }
           showListAdapter(it.verses, false)
       }
    }

    private fun onReadMore() {
       var isExpanded = false
       binding?.apply {
           mtvReadMore.setOnClickListener {
               if(!isExpanded){
                 isExpanded = true
                 mtvVersesCDesc.maxLines = 50
                 mtvReadMore.text = getString(R.string.read_less)
               } else {
                  isExpanded = false
                  mtvVersesCDesc.maxLines = 4
                  mtvReadMore.text = getString(R.string.read_more)
               }
           }
       }
    }

    private fun receiveData() {
        val bundle = arguments
        if (bundle != null) {
            chapterNo = bundle.getInt("chapterNumber")
            binding?.apply {
                mtvVersesChapter.text = String.format("Chapter $chapterNo")
                mtvVersesCName.text = bundle.getString("chapterTitle")
                mtvVersesCDesc.text = bundle.getString("chapterDesc")
                mtvNoVerses.text = String.format(" ${bundle.getInt("verseCount")} Verses")
            }
        }
    }

    private fun getAllVerses() {
       lifecycleScope.launch {
          viewModels.getAllVerses(chapterNo).collect {
             val verseList = arrayListOf<String>()
             for(currentVerse in  it){
                for(verses in currentVerse.translations){
                   if(verses.language == "english"){
                      verseList.add(verses.description)
                      break
                   }
                }
             }
              showListAdapter(verseList, true)
          }
       }
    }

    private fun showListAdapter(verseList: List<String>, onClick:Boolean) {
        binding?.apply {
            rvVerses.apply {
                versesAdapter = VersesAdapter(::onVersesClicked, onClick)
                adapter = versesAdapter
            }
            versesShimmer.visibility = View.GONE
            rvVerses.visibility = View.VISIBLE
            versesAdapter.asyncDiffUtil.submitList(verseList)
        }
    }

    private fun checkInternet() {
        val networkManager = NetworkManager(requireContext())
        networkManager.observe(viewLifecycleOwner) {
            if(it == true){
                getAllVerses()
                binding?.apply {
                    versesShimmer.visibility = View.VISIBLE
                    mtvNoInternet.visibility = View.GONE
                    rvVerses.visibility = View.VISIBLE
                }
            }else {
                binding?.apply {
                    mtvNoInternet.visibility = View.VISIBLE
                    versesShimmer.visibility = View.GONE
                    rvVerses.visibility = View.GONE
                }
            }
        }
    }

    private fun onVersesClicked(verse:String, verseNo:Int){
       val bundle = Bundle()
       bundle.putInt("chapterNum", chapterNo)
       bundle.putInt("verseNum", verseNo)
       findNavController().navigate(R.id.action_versesFragment_to_versesDetailFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeechManager.stop(true)
    }
}