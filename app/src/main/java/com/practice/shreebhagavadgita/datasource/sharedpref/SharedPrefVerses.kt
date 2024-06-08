package com.practice.shreebhagavadgita.datasource.sharedpref

import android.content.Context
import android.content.SharedPreferences

class SharedPrefVerses(context: Context) {

    private val sharedPrefVerses : SharedPreferences by lazy {
       context.getSharedPreferences("saved_verses", Context.MODE_PRIVATE)
    }

    fun getAllSavedVersesKeySP(): Set<String> {
        return sharedPrefVerses.all.keys
    }

    fun putSavedVersesSP(key: String, value: Int) {
       sharedPrefVerses.edit().putInt(key,value).apply()
    }

    fun deleteSavedVersesSP(key:String) {
       sharedPrefVerses.edit().remove(key).apply()
    }
}