package com.practice.shreebhagavadgita.datasource.api

import com.practice.shreebhagavadgita.datamodels.chaptersdata.ChaptersDataItem
import com.practice.shreebhagavadgita.datamodels.versesdata.VersesDataItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("v2/chapters/")
    fun getAllChapters() : Call<List<ChaptersDataItem>>

    @GET("v2/chapters/{chapterNumber}/verses/")
    fun getAllVerses(@Path("chapterNumber") chapterNumber:Int) : Call<List<VersesDataItem>>

    @GET("v2/chapters/{chapterNumber}/verses/{verseNum}/")
    fun getParticularVerse(@Path("chapterNumber") chapterNumber: Int, @Path("verseNum") verseNum:Int) : Call<VersesDataItem>

}