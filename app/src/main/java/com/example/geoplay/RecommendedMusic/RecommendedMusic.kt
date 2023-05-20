package com.example.geoplay.RecommendedMusic

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.geoplay.LocationHelper
import com.example.geoplay.Song
import com.example.geoplay.SongPlayer.SongPlayer
import com.example.geoplay.SongPlayer.TrackedSongPlayerSongs
import com.example.geoplay.SpotifyApiHandler
import com.example.geoplay.reusable.SongCover
import kotlinx.coroutines.runBlocking
import java.io.Serializable

@Composable
fun RecommendedMusic() {
    val songs = loadSongs(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1A))
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Preporučeno",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        SongsContainer(songs)
    }
}

fun loadSongs(context: Context): MutableList<Song> {
    val songs: MutableList<Song> = mutableListOf()

    val locationHelper = LocationHelper(context)
    locationHelper.startLocationUpdates()
    locationHelper.updateCityName()

    runBlocking {
        val musicApi = SpotifyApiHandler()
        musicApi.buildSearchApi()
        val res = musicApi.trackSearch(locationHelper.cityName)

        for (it in res.tracks!!) {
            var track = it

            var currReplaced = res.tracks!!.size - 1
            if (track.previewUrl == null) {
                track = res.tracks!![currReplaced]
                currReplaced -= 1
                continue
            }
            val artists = track.artists.map { it.name }
            val img = track.album.images[0].url
            songs.add(Song(track.name, artists as ArrayList<String>, img, track.previewUrl))
        }
    }

    locationHelper.stopLocationUpdates()
    return songs
}

fun onSongClicked(context: Context, trackedSongPlayerSongs: TrackedSongPlayerSongs) {
    val intent = Intent(context, SongPlayer::class.java)
    intent.putExtra("trackedSongPlayerSongs", trackedSongPlayerSongs as Serializable)
    context.startActivity(intent)
}

@Composable
fun SongContainer(trackedSongPlayerSongs: TrackedSongPlayerSongs) {
    val context = LocalContext.current

    val song = trackedSongPlayerSongs.songPlayerSongs[trackedSongPlayerSongs.currentIndex]

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .clickable { onSongClicked(context, trackedSongPlayerSongs) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                SongCover(imageUrl = song.imageUrl)
            }
            Divider(color = Color.hsv(356F, 0.7F, 0.98F), thickness = 4.dp)
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artists.joinToString(", "),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SongsContainer(songs: MutableList<Song>) {

    val startIndex = remember { mutableStateOf(0) }
    val endIndex = remember { mutableStateOf(9) }

    val updateRange: (command: RecommendedMusicSwapCommand) -> Unit = {
        val movement = 10
        if (it == RecommendedMusicSwapCommand.INCREASE) {
            if (songs.size < endIndex.value + movement) {
                startIndex.value = 0
                endIndex.value = movement - 1
            } else {
                startIndex.value += movement
                endIndex.value += movement
            }
        } else if (it == RecommendedMusicSwapCommand.DECREASE) {
            if (startIndex.value - movement < 0) {
                startIndex.value = songs.size - (movement - 1)
                endIndex.value = songs.size - 1
            } else {
                startIndex.value -= movement
                endIndex.value -= movement
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp, 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = { updateRange(RecommendedMusicSwapCommand.DECREASE) }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }
        IconButton(onClick = { updateRange(RecommendedMusicSwapCommand.INCREASE) }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }
    }

    val chunkSize = 2
    val indices = startIndex.value until endIndex.value
    val chunks = indices.chunked(chunkSize)
    chunks.forEach { chunk ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            chunk.forEach { index ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(15.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    SongContainer(
                        TrackedSongPlayerSongs(songs, index)
                    )
                }
            }
        }
    }
}