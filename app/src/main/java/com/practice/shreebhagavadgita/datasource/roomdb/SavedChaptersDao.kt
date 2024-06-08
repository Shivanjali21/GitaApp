package com.practice.shreebhagavadgita.datasource.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChaptersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(savedChapters: SavedChapters)

    @Query("SELECT * FROM SavedChapters")
    fun getSavedChapters() : LiveData<List<SavedChapters>>

    @Query("DELETE FROM SavedChapters WHERE id = :id")
    suspend fun deleteChapter(id: Int)

    @Query("SELECT * FROM SavedChapters WHERE chapter_number = :chapterNumber")
    fun getParticularChapter(chapterNumber: Int) : LiveData<SavedChapters>
}

@Dao
interface SavedVersesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnglishVerses(savedVerses: SavedVerses)

    @Query("SELECT * FROM SavedVerses")
    fun getAllEnglishVerses() : LiveData<List<SavedVerses>>

    @Query("DELETE FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number =:verseNumber")
    fun deleteAParticularVerse(chapterNumber: Int, verseNumber:Int)

    @Query("SELECT * FROM SavedVerses WHERE chapter_number = :chapterNumber AND verse_number =:verseNumber")
    fun getParticularVerses(chapterNumber: Int, verseNumber:Int) : LiveData<SavedVerses>
}