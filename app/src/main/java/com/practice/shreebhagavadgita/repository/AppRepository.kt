package com.practice.shreebhagavadgita.repository

import androidx.lifecycle.LiveData
import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.datamodels.versesdata.VersesDataItem
import com.practice.shreebhagavadgita.datasource.api.ApiUtilities
import com.practice.shreebhagavadgita.datasource.roomdb.SavedChapters
import com.practice.shreebhagavadgita.datasource.roomdb.SavedChaptersDao
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVerses
import com.practice.shreebhagavadgita.datasource.roomdb.SavedVersesDao
import com.practice.shreebhagavadgita.datasource.sharedpref.SharedPrefChapters
import com.practice.shreebhagavadgita.datasource.sharedpref.SharedPrefVerses
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppRepository(private val savedChaptersDao: SavedChaptersDao, private val savedVersesDao: SavedVersesDao,
                    private val sharedPrefChapter : SharedPrefChapters, private val sharedPrefVerses: SharedPrefVerses) {

    fun getAllChapters() : Flow<List<ChaptersDataItem>> = callbackFlow {
       val callBack = object : Callback<List<ChaptersDataItem>> {
           override fun onResponse(call: Call<List<ChaptersDataItem>>, response: Response<List<ChaptersDataItem>>) {
               if(response.isSuccessful && response.body() != null){
                  trySend(response.body()!!)
                  close()
               }
           }
           override fun onFailure(call: Call<List<ChaptersDataItem>>, t: Throwable) {
              close(t)
           }
       }
       ApiUtilities.api.getAllChapters().enqueue(callBack)
       awaitClose {}
    }

    fun getVerses(chaptersNumber:Int) : Flow<List<VersesDataItem>> = callbackFlow {
       val callback = object : Callback<List<VersesDataItem>> {
           override fun onResponse(call: Call<List<VersesDataItem>>, response: Response<List<VersesDataItem>>) {
               if(response.isSuccessful && response.body() != null){
                   trySend(response.body()!!)
                   close()
               }
           }
           override fun onFailure(call: Call<List<VersesDataItem>>, t: Throwable) {
              close(t)
           }
       }
       ApiUtilities.api.getAllVerses(chaptersNumber).enqueue(callback)
       awaitClose { }
    }

    fun getParticularVerse(chaptersNumber:Int, verseNumber:Int) : Flow<VersesDataItem> = callbackFlow {
        val callback = object : Callback<VersesDataItem> {
            override fun onResponse(call: Call<VersesDataItem>, response: Response<VersesDataItem>) {
                if(response.isSuccessful && response.body() != null){
                    trySend(response.body()!!)
                    close()
                }
            }
            override fun onFailure(call: Call<VersesDataItem>, t: Throwable) {
                close(t)
            }
        }
        ApiUtilities.api.getParticularVerse(chaptersNumber, verseNumber).enqueue(callback)
        awaitClose { }
    }

    /*For Saved Chapters in Room*/

    suspend fun insertChapters(savedChapters: SavedChapters) = savedChaptersDao.insertChapters(savedChapters)

    fun getSavedChapters() : LiveData<List<SavedChapters>> = savedChaptersDao.getSavedChapters()

    fun getAParticularChapter(chapterNumber:Int) : LiveData<SavedChapters> = savedChaptersDao.getParticularChapter(chapterNumber)

    suspend fun deleteChapter(id: Int) = savedChaptersDao.deleteChapter(id)


    /*For Saved Verses in Room*/
    suspend fun insertEnglishVerses(savedVerses: SavedVerses) = savedVersesDao.insertEnglishVerses(savedVerses)

    fun getAllEnglishVerses() : LiveData<List<SavedVerses>> = savedVersesDao.getAllEnglishVerses()

    fun deleteAParticularVerse(chapterNumber: Int, verseNumber:Int) = savedVersesDao.deleteAParticularVerse(chapterNumber, verseNumber)

    fun getParticularVerses(chapterNumber: Int, verseNumber:Int) : LiveData<SavedVerses> = savedVersesDao.getParticularVerses(chapterNumber, verseNumber)


    /*save chapter in SP*/
    fun getAllSavedChapterKeySP(): Set<String> = sharedPrefChapter.getAllSavedChapterKeySP()
    fun putSavedChapterSP(key: String, value: Int) = sharedPrefChapter.putSavedChapterSP(key, value)
    fun deleteSavedChapterSP(key:String) = sharedPrefChapter.deleteSavedChapterSP(key)

    /*save verses in SP*/
    fun getAllSavedVersesKeySP(): Set<String> = sharedPrefVerses.getAllSavedVersesKeySP()
    fun putSavedVersesSP(key: String, value: Int) = sharedPrefVerses.putSavedVersesSP(key, value)
    fun deleteSavedVersesSP(key:String) = sharedPrefVerses.deleteSavedVersesSP(key)
}