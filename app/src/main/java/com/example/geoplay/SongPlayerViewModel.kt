package com.example.geoplay

import androidx.compose.runtime.mutableStateOf
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
        _uiState.value = SongPlayerState(null, false, null)
    }

    fun updatePlayer(newPlayer: ExoPlayer) {
        _uiState.update { currentState ->
            currentState.copy(
                player = newPlayer
            )
        }
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

    fun updateSong(newSong: Song) {
        _uiState.update { currentState ->
            currentState.copy(
                song = newSong
            )
        }
    }
}