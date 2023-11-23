package com.example.loop_new.data

import android.media.MediaPlayer
import com.example.loop_new.domain.services.InterfaceService
import java.io.IOException

class Service: InterfaceService {
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