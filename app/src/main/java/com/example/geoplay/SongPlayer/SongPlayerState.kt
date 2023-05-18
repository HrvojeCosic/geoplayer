package com.example.geoplay.SongPlayer

import com.example.geoplay.Song
import com.google.android.exoplayer2.ExoPlayer

data class SongPlayerState (
    val player: ExoPlayer? = null,
    val isPlaying: Boolean = false,
    val song: Song? = null,
    val progression: Float = 0F
)