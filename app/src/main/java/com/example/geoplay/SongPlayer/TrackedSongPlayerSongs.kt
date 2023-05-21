package com.example.geoplay.SongPlayer

import com.example.geoplay.RecommendedMusic.Song
import java.io.Serializable

data class TrackedSongPlayerSongs (
    val songPlayerSongs: MutableList<Song>,
    val currentIndex: Int
) : Serializable