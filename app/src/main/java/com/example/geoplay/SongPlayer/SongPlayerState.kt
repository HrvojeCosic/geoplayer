package com.example.geoplay.SongPlayer

import com.google.android.exoplayer2.ExoPlayer

data class SongPlayerState (
    val player: ExoPlayer? = null,
    val isPlaying: Boolean = false,
    val progression: Float = 0F,
    val trackedSongPlayerSongs: TrackedSongPlayerSongs? = null
)