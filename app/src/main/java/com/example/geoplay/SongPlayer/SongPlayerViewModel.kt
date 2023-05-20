package com.example.geoplay.SongPlayer

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.os.Handler
import com.google.android.exoplayer2.Player

class SongPlayerViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SongPlayerState())
    val uiState: StateFlow<SongPlayerState> = _uiState.asStateFlow()

    init {
        _uiState.value = SongPlayerState()
    }

    fun setupPlayer(trackedSongPlayerSongs: TrackedSongPlayerSongs, player: ExoPlayer? = _uiState.value.player): ExoPlayer? {
        if (player == null) { return null }

        val song = trackedSongPlayerSongs.songPlayerSongs[trackedSongPlayerSongs.currentIndex]
        val mediaItem = MediaItem.fromUri(song.playbackUrl!!)
        player.setMediaItem(mediaItem)
        player.prepare()

        updatePlayer(player)
        updateTrackedSongPlayerSongs(trackedSongPlayerSongs)

        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                updateProgress(
                    (player.currentPosition*100/player.duration).toFloat(),
                    SongPlayerViewModel.UpdateCause.PROCESS
                )
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(runnable,0)

        player.addListener(object: Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED && _uiState.value.isPlaying) {
                    toggleSongPlay()
                }
            }
        })
        return player
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

    fun updateSongIndex(newIndex: Int) {
        _uiState.update { currentState ->
            if (newIndex < 0 || newIndex >= currentState.trackedSongPlayerSongs!!.songPlayerSongs.size) { return }
            currentState.copy(
                trackedSongPlayerSongs = TrackedSongPlayerSongs(currentState.trackedSongPlayerSongs.songPlayerSongs, newIndex),
            )
        }

        _uiState.update { currentState ->
            if (newIndex < 0 || newIndex >= currentState.trackedSongPlayerSongs!!.songPlayerSongs.size) { return }
            currentState.copy(
                player = setupPlayer(currentState.trackedSongPlayerSongs)
            )
        }
    }

    private fun updateTrackedSongPlayerSongs(trackedSongPlayerSongs: TrackedSongPlayerSongs) {
        _uiState.update { currentState ->
            currentState.copy(
                trackedSongPlayerSongs = trackedSongPlayerSongs
            )
        }
    }

    private fun updatePlayer(newPlayer: ExoPlayer) {
        _uiState.update { currentState ->
            currentState.copy(
                player = newPlayer
            )
        }
    }
}