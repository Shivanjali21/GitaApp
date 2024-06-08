package com.practice.shreebhagavadgita.datasource.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.shreebhagavadgita.datamodels.versesdata.Commentary
import com.practice.shreebhagavadgita.datamodels.versesdata.Translation

@Entity(tableName = "SavedChapters")
data class SavedChapters(
    @PrimaryKey
    val id: Int,
    val chapter_number: Int,
    val chapter_summary: String,
    val chapter_summary_hindi: String,
    val name: String,
    val name_meaning: String,
    val name_translated: String,
    val name_transliterated: String,
    val slug: String,
    val verses_count: Int,

    val verses : List<String> //custom added
)

@Entity(tableName = "SavedVerses")
data class SavedVerses(
    val chapter_number: Int,
    val commentaries: List<Commentary>,
    @PrimaryKey
    val id: Int,
    val slug: String,
    val text: String,
    val translations: List<Translation>,
    val transliteration: String,
    val verse_number: Int,
    val word_meanings: String
)
