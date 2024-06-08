package com.practice.shreebhagavadgita.datasource.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SharedPrefChapters(context: Context) {

    private val sharedPrefChapters : SharedPreferences by lazy {
       context.getSharedPreferences("saved_chapters", Context.MODE_PRIVATE)
    }

    fun getAllSavedChapterKeySP(): Set<String> {
       return sharedPrefChapters.all.keys
    }

    fun putSavedChapterSP(key: String, value: Int) {
       sharedPrefChapters.edit().putInt(key, value).apply()
    }

    fun deleteSavedChapterSP(key:String) {
       sharedPrefChapters.edit().remove(key).apply()
    }
}