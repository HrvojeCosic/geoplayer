package com.example.geoplay

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SongPlayerViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SongPlayerState())
    val uiState: StateFlow<SongPlayerState> = _uiState.asStateFlow()

    init {
        _uiState.value = SongPlayerState(null, false, null, 0F)
    }

    fun toggleSongPlay() {
        _uiState.update { currentState ->
            if (currentState.isPlaying) {
                currentState.player!!.pause()
            } else {
                currentState.player!!.play()
            }
            currentState.copy(isPlaying = !currentState.isPlaying)
        }
    }

    enum class UpdateCause {
        USER {},
        PROCESS {}
    }
    fun updateProgress(progress: Float, cause: UpdateCause) {
        _uiState.update { currentState ->
            val updatedPlayer = currentState.player

            if (cause == UpdateCause.USER) {
                val newPosition = currentState.player?.duration!! * (progress / 100)
                updatedPlayer?.seekTo(newPosition .toLong())
            }

            currentState.copy(
                progression = progress,
                player = updatedPlayer
            )
        }
    }

    fun updateSong(newSong: Song) {
        _uiState.update { currentState ->
            currentState.copy(
                song = newSong
            )
        }
    }

    fun updatePlayer(newPlayer: ExoPlayer) {
        _uiState.update { currentState ->
            currentState.copy(
                player = newPlayer
            )
        }
    }
}