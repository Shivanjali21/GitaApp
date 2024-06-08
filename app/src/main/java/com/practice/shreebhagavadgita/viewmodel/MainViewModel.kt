package com.practice.shreebhagavadgita.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.datamodels.versesdata.VersesDataItem
import com.practice.shreebhagavadgita.datasource.roomdb.BhagavadGitaDatabase
import com.practice.shreebhagavadgita.datasource.roomdb.SavedChapters
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVerses
import com.practice.shreebhagavadgita.datasource.sharedpref.SharedPrefChapters
import com.practice.shreebhagavadgita.datasource.sharedpref.SharedPrefVerses
import com.practice.shreebhagavadgita.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(application:Application) : AndroidViewModel(application) {

    private val savedChaptersDao = BhagavadGitaDatabase.getDatabaseInstance(application)?.savedChaptersDao()
    private val savedVersesDao = BhagavadGitaDatabase.getDatabaseInstance(application)?.savedVersesDao()
    private val sharedPrefChapters = SharedPrefChapters(application)
    private val sharedPrefVerses = SharedPrefVerses(application)
    private val appRepository = AppRepository(savedChaptersDao!!, savedVersesDao!!, sharedPrefChapters, sharedPrefVerses)

    fun getAllChapters() : Flow<List<ChaptersDataItem>> = appRepository.getAllChapters()

    fun getAllVerses(chapterNumber : Int) : Flow<List<VersesDataItem>> = appRepository.getVerses(chapterNumber)

    fun getAParticularVerse(chaptersNumber:Int, verseNumber:Int) : Flow<VersesDataItem> = appRepository.getParticularVerse(chaptersNumber, verseNumber)

    //saved Chapters in room db
    suspend fun insertChapters(savedChapters: SavedChapters) = appRepository.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = appRepository.getSavedChapters()

    fun getAParticularChapter(chapterNumber: Int) : LiveData<SavedChapters> = appRepository.getAParticularChapter(chapterNumber)

    suspend fun deleteChapter(id:Int) = appRepository.deleteChapter(id)

    /*saved verses in room db*/
    suspend fun insertEnglishVerses(savedVerses: SavedVerses) = appRepository.insertEnglishVerses(savedVerses)

    fun getSavedEnglishVerses() : LiveData<List<SavedVerses>> = appRepository.getAllEnglishVerses()

    fun deleteAParticularVerse(chapterNumber: Int, verseNumber:Int) = appRepository.deleteAParticularVerse(chapterNumber, verseNumber)

    fun getParticularVerses(chapterNumber: Int, verseNumber:Int) : LiveData<SavedVerses> = appRepository.getParticularVerses(chapterNumber, verseNumber)

    /*saved chapter in SP*/
    fun getAllSavedChapterKeySP(): Set<String> = appRepository.getAllSavedChapterKeySP()
    fun putSavedChapterSP(key: String, value: Int) = appRepository.putSavedChapterSP(key, value)
    fun deleteSavedChapterSP(key:String) = appRepository.deleteSavedChapterSP(key)

    /*save verses in SP*/
    fun getAllSavedVersesKeySP(): Set<String> = appRepository.getAllSavedVersesKeySP()
    fun putSavedVersesSP(key: String, value: Int) = appRepository.putSavedVersesSP(key, value)
    fun deleteSavedVersesSP(key:String) = appRepository.deleteSavedVersesSP(key)

}