package com.example.geoplay

import com.google.android.exoplayer2.ExoPlayer

data class SongPlayerState (
    val player: ExoPlayer? = null,
    val isPlaying: Boolean = false,
    val song: Song? = null,
)