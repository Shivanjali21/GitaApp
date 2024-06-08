package com.practice.shreebhagavadgita.ui.fragments.settings.savedverses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.shreebhagavadgita.databinding.RvVersesItemBinding
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVerses

class SavedVersesAdapter (private val onVerseClicked: (SavedVerses) -> Unit) : RecyclerView.Adapter<SavedVersesAdapter.SVersesViewHolder>() {

    private val diffUtils = object : DiffUtil.ItemCallback<SavedVerses>() {
        override fun areItemsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SavedVerses, newItem: SavedVerses): Boolean {
            return oldItem == newItem
        }
    }

    val asyncDiffUtil = AsyncListDiffer(this, diffUtils)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVersesAdapter.SVersesViewHolder {
        val view = RvVersesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SVersesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedVersesAdapter.SVersesViewHolder, position: Int) {
        val savedVerse = asyncDiffUtil.currentList[position]
        holder.binding.apply {
            mtvVersesNo.text = String.format("Verse ${savedVerse.chapter_number}.${savedVerse.verse_number}")
            mtvVersesDetail.text = savedVerse.translations[0].description
            mcvVersesRoot.setOnClickListener {
              onVerseClicked(savedVerse)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncDiffUtil.currentList.size
    }

    inner class SVersesViewHolder (val binding : RvVersesItemBinding) : RecyclerView.ViewHolder(binding.mcvVersesRoot)
}