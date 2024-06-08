package com.practice.shreebhagavadgita.ui.fragments.verses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.shreebhagavadgita.databinding.RvVersesItemBinding

class VersesAdapter(private val onVerseClicked: (String, Int) -> Unit, private val onClick: Boolean) : RecyclerView.Adapter<VersesAdapter.ChaptersViewHolder>() {

    private val diffUtils = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
          return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
           return oldItem == newItem
        }
    }

    val asyncDiffUtil = AsyncListDiffer(this, diffUtils)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersesAdapter.ChaptersViewHolder {
       val view = RvVersesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ChaptersViewHolder(view)
    }

    override fun onBindViewHolder(holder: VersesAdapter.ChaptersViewHolder, position: Int) {
       val verse = asyncDiffUtil.currentList[position]
       holder.binding.apply {
          mtvVersesNo.text = String.format("Verse ${position + 1}")
          mtvVersesDetail.text = verse
          mcvVersesRoot.setOnClickListener {
             if(onClick){
                onVerseClicked(verse, position + 1)
             }
          }
           if(onClick){
               ivArrowNext.visibility = View.VISIBLE
           }else {
               ivArrowNext.visibility = View.GONE
           }
       }
    }

    override fun getItemCount(): Int {
       return asyncDiffUtil.currentList.size
    }

    inner class ChaptersViewHolder (val binding : RvVersesItemBinding) : RecyclerView.ViewHolder(binding.mcvVersesRoot)
}