package com.example.myapplication.Utility

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import android.R.attr.data

@Suppress("DEPRECATION")

class MediaPlay {

    companion object {
        val instance = MediaPlay()
        var mediaPlayer = MediaPlayer()
    }

    private var playbackPosition = 0

    fun playMedia(url:String){
        try {
            releaseMediaPlayer()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopMedia(){
        mediaPlayer.stop()
        playbackPosition = 0
    }

    fun pauseMedia(){
        playbackPosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
    }

    fun restartMedia(){
        mediaPlayer.seekTo(playbackPosition)
        mediaPlayer.start()
    }
    private fun releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}