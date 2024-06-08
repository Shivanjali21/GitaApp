package com.practice.shreebhagavadgita.datasource.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practice.shreebhagavadgita.datamodels.versesdata.Commentary
import com.practice.shreebhagavadgita.datamodels.versesdata.Translation

class BGitaTypeConverters {

    @TypeConverter
    fun fromListToString(list: List<String>) : String {
       return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToList(string : String) : List<String> {
       return Gson().fromJson(string,  object : TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromTransToString(list: List<Translation>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToTrans(string : String) : List<Translation> {
        return Gson().fromJson(string,  object : TypeToken<List<Translation>>(){}.type)
    }

    @TypeConverter
    fun fromCommentToString(list: List<Commentary>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToComment(string : String) : List<Commentary> {
        return Gson().fromJson(string,  object : TypeToken<List<Commentary>>(){}.type)
    }

}