package com.example.geoplay.Playlist

import com.example.geoplay.RecommendedMusic.Song

data class SongFirebase (
    val song: Song,
    val ref: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SongFirebase) return false

        return song == other.song
    }

    override fun hashCode(): Int {
        return song.hashCode()
    }
}