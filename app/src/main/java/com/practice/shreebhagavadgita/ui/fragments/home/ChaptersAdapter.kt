package com.practice.shreebhagavadgita.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.shreebhagavadgita.databinding.RvChaptersItemBinding
import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.viewmodel.MainViewModel

class ChaptersAdapter(
    private val onChapterClicked: (ChaptersDataItem) -> Unit,
    private val onFavouriteClicked: (ChaptersDataItem) -> Unit,
    private val showSaveButton: Boolean,
    private val onFFavClicked: (ChaptersDataItem) -> Unit,
    private val viewModels: MainViewModel,
) : RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {

    private val diffUtils = object : DiffUtil.ItemCallback<ChaptersDataItem>() {
        override fun areItemsTheSame(oldItem: ChaptersDataItem, newItem: ChaptersDataItem): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersDataItem, newItem: ChaptersDataItem): Boolean {
           return oldItem.id == newItem.id
        }
    }

    val asyncDiffUtil = AsyncListDiffer(this, diffUtils)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersAdapter.ChaptersViewHolder {
       val view = RvChaptersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ChaptersViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChaptersAdapter.ChaptersViewHolder, position: Int) {
       holder.binding.apply {
          val chapter = asyncDiffUtil.currentList[position]
          mtvChaptersNo.text = String.format("Chapter ${chapter.chapter_number}")
          mtvCTitle.text = chapter.name_translated
          mtvCDescription.text = chapter.chapter_summary
          mtvCVerseNo.text = String.format(" ${chapter.verses_count} Verses")
          mcvRootChapters.setOnClickListener {
            onChapterClicked(chapter)
          }
           //Fav Clicked
           ivFavourite.setOnClickListener {
              ivFFavourite.visibility = View.VISIBLE
              ivFavourite.visibility = View.GONE
              onFavouriteClicked.invoke(chapter)
           }

           //Fill Fav
           ivFFavourite.setOnClickListener {
              ivFavourite.visibility = View.VISIBLE
              ivFFavourite.visibility = View.GONE
              onFFavClicked(chapter)
           }

           val keys = viewModels.getAllSavedChapterKeySP()
           if(!showSaveButton) {
              ivFavourite.visibility = View.GONE
              ivFFavourite.visibility = View.GONE
           } else {
              if(keys.contains(chapter.chapter_number.toString())){
                ivFavourite.visibility = View.GONE
                ivFFavourite.visibility = View.VISIBLE
              }else {
                 ivFavourite.visibility = View.VISIBLE
                 ivFFavourite.visibility = View.GONE
              }
           }
       }
    }

    override fun getItemCount(): Int {
       return asyncDiffUtil.currentList.size
    }

    inner class ChaptersViewHolder (val binding : RvChaptersItemBinding) : RecyclerView.ViewHolder(binding.mcvRootChapters)
}