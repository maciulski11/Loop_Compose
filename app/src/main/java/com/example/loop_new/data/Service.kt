package com.example.loop_new.data

import android.media.MediaPlayer
import com.example.loop_new.domain.services.Service
import java.io.IOException

class Service : Service {
    override fun playAudioFromUrl(audioUrl: String) {

        MediaPlayer().apply {
            try {
                setDataSource(audioUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}