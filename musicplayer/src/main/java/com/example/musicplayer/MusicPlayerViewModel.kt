package com.example.musicplayer

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PlayerState {
    IDLE, PLAYING, PAUSED, STOPPED, ERROR
}

class MusicPlayerViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    private val _playerState = MutableStateFlow(PlayerState.IDLE)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val musicUrls = listOf(
        "http://music.163.com/song/media/outer/url?id=447925558.mp3",
        "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/music/audio.mp3"
    )
    private var currentMusicIndex = 0

    private val _currentMusicUrl = MutableStateFlow(musicUrls[currentMusicIndex])
    val currentMusicUrl: StateFlow<String> = _currentMusicUrl.asStateFlow()

    fun switchSource() {
        stopPlaying()
        currentMusicIndex = (currentMusicIndex + 1) % musicUrls.size
        _currentMusicUrl.value = musicUrls[currentMusicIndex]
    }

    fun startPlaying(onSessionIdReady: (Int) -> Unit) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(musicUrls[currentMusicIndex])
                    setOnPreparedListener { mp ->
                        mp.start()
                        _playerState.value = PlayerState.PLAYING
                        // 获取音频会话ID并通过回调传递
                        onSessionIdReady(mp.audioSessionId)
                    }
                    setOnCompletionListener {
                        _playerState.value = PlayerState.STOPPED
                    }
                    setOnErrorListener { _, _, _ ->
                        _playerState.value = PlayerState.ERROR
                        true
                    }
                    prepareAsync()
                }
            } else {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(musicUrls[currentMusicIndex])
                mediaPlayer?.prepareAsync()
            }
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
            e.printStackTrace()
        }
    }

    fun pausePlaying() {
        try {
            mediaPlayer?.pause()
            _playerState.value = PlayerState.PAUSED
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
            e.printStackTrace()
        }
    }

    fun resumePlaying() {
        try {
            mediaPlayer?.start()
            _playerState.value = PlayerState.PLAYING
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
            e.printStackTrace()
        }
    }

    fun stopPlaying() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            _playerState.value = PlayerState.STOPPED
        } catch (e: Exception) {
            _playerState.value = PlayerState.ERROR
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
