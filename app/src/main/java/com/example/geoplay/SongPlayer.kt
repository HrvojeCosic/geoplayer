package com.example.geoplay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.geoplay.ui.theme.SeekBar
import com.google.accompanist.coil.rememberCoilPainter

class SongPlayer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val song: Song = loadSongFromIntent(intent)

        setContent {
            Column(
                modifier = Modifier
                    .background(Color(0xFF1B1B1A))
            ) {
                BackArrow { finish() }
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .size(600.dp)
                            .padding(64.dp)
                            .align(Alignment.Center)
                    ) {
                        Box( modifier = Modifier.align(Alignment.TopCenter) ) {
                            AlbumCover(song.imageUrl)
                        }
                        SongInfo(song, modifier = Modifier.align(Alignment.Center))
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(0.dp, 70.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SeekBar()
                                Controls()
                            }
                        }
                    }
                }
            }
        }
    }
}

fun onBackClicked(context: Context) {
    val intent = Intent(context, SongPlayer::class.java)
    context.startActivity(intent)

}

@Composable
fun Controls() {
    Row {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Previous",
            tint = Color.White,
            modifier = Modifier.size(47.dp)
        )
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = Color.White,
            modifier = Modifier.size(47.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Previous",
            tint = Color.White,
            modifier = Modifier.size(47.dp)
        )
    }
}

fun loadSongFromIntent(intent: Intent): Song {
    val songTitle = intent.getStringExtra("title").orEmpty()
    val songArtists = intent.getStringArrayListExtra("artists") as ArrayList<String>
    val imageUrl = intent.getStringExtra("imageURL").orEmpty()
    return Song(songTitle, songArtists, imageUrl)
}

@Composable
fun SongInfo(song: Song, modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = song.title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
        Text(
            text = song.artists.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
        )
    }
}

@Composable
fun AlbumCover(imageUrl: String) {
    val painter: Painter = rememberCoilPainter(request = imageUrl)
    Image(
        painter = painter,
        contentDescription = "Image from ${imageUrl}",
        modifier = Modifier
            .size(200.dp)
            .clip(RoundedCornerShape(20.dp))
    )
}

@Composable
fun BackArrow(onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = Modifier
            .padding(16.dp)
            .size(40.dp)
    ) {
        Icon(
            modifier = Modifier.size(45.dp),
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}