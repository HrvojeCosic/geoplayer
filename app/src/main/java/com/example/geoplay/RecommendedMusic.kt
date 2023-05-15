package com.example.geoplay

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.geoplay.reusable.SongCover
import com.example.geoplay.reusable.loadRandomColor
import kotlinx.coroutines.runBlocking

@Composable
fun RecommendedMusic() {
    val songs = loadSongs()

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
        CollectionSwapper()
        SongsContainer(songs)
    }
}

fun loadSongs(): MutableList<Song> {
    val songs: MutableList<Song> = mutableListOf()
    runBlocking {
        val musicApi = SpotifyApiHandler()
        musicApi.buildSearchApi()
        val res = musicApi.trackSearch("sandstorm")


        for (it in res.tracks.orEmpty()) {
            if (it == null) {
                continue
            }
            val artists = it.artists.map { it.name }
            val img = it.album.images[0].url
            songs.add(Song(it.name, artists as ArrayList<String>, img))
        }
    }
    return songs
}

fun onSongClicked(context: Context, song: Song) {
    val intent = Intent(context, SongPlayer::class.java)
    intent.putExtra("title", song.title)
    intent.putStringArrayListExtra("artists", song.artists)
    intent.putExtra("imageURL", song.imageUrl)
    context.startActivity(intent)
}

@Composable
fun SongContainer(song: Song) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(Color.LightGray)
            .clickable { onSongClicked(context, song) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f) // Occupy available space
            ) {
                SongCover(imageUrl = song.imageUrl)
            }
            // TODO: Loads slowly due to loading random colors 1 by 1
            Divider(color = loadRandomColor(), thickness = 4.dp)
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
fun CollectionSwapper() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.dp, 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Previous",
            tint = Color.White,
            modifier = Modifier.size(47.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Next",
            tint = Color.White,
            modifier = Modifier.size(47.dp)
        )
    }
}

@Composable
fun SongsContainer(songs: MutableList<Song>) {
    LazyColumn() {
        itemsIndexed(songs.chunked(2)) { _, songsChunk ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                songsChunk.forEach { song ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(15.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        SongContainer(song)
                    }
                }
            }
        }
    }
}