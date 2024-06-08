package com.practice.shreebhagavadgita.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.widget.Toast
import timber.log.Timber
import java.util.Locale

class TextToSpeechManager(private val context: Context) {

   private var textToSpeech : TextToSpeech ? = null
   private var isInitialize = false

   init {
       textToSpeech = TextToSpeech(context) { status ->
           if (status == TextToSpeech.SUCCESS) {
               val result = textToSpeech?.setLanguage(Locale.ENGLISH)
               if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                  Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
               } else {
                  for(voice in textToSpeech?.voices!!){
                    Timber.tag("Voices").d(voice.name)
                  }
                  val voice = Voice("hi-in-hie-local", Locale("hi_IN"), 400, 200, false, HashSet(
                    listOf("male")
                  ))
                  textToSpeech?.voice = voice
                  isInitialize = true
               }
           } else {
               Toast.makeText(context, "Initialization Error!", Toast.LENGTH_SHORT).show()
           }
       }
   }

   fun speak(text:String) {
       if (isInitialize) {
           textToSpeech?.apply {
               setSpeechRate(1.0f)
               setPitch(0.8f)
               speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
           }
       } else {
           Toast.makeText(context, "Initialization Error!", Toast.LENGTH_SHORT).show()
       }
   }

    fun stop(shutDown: Boolean) {
        if (isInitialize) {
            textToSpeech?.stop()
            if (shutDown) textToSpeech?.shutdown()
        } else {
            Toast.makeText(context, "Initialization Error!", Toast.LENGTH_SHORT).show()
        }
    }

    fun setUtteranceProgressListener (listener : UtteranceProgressListener) {
       textToSpeech?.setOnUtteranceProgressListener(listener)
    }
}