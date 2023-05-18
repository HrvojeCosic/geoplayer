package com.example.geoplay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.geoplay.reusable.SongCover
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player


class SongPlayer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val song = loadSongFromIntent(intent)
        val playerViewModel = ViewModelProvider(this)[SongPlayerViewModel::class.java]
        initializePlayer(this, song, playerViewModel)

        setContent {
            Column( modifier = Modifier.background(Color(0xFF1B1B1A)) ) {
                BackArrow {
                    playerViewModel.uiState.value.player?.stop()
                    finish()
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .size(600.dp)
                            .padding(64.dp)
                            .align(Alignment.Center)
                    ) {
                        Box(modifier = Modifier.align(Alignment.TopCenter)) {
                            SongCover(song.imageUrl)
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
                                ProgressBar()
                                Controls()
                            }
                        }
                    }
                }
            }
        }}
}

@Composable
fun Controls(playerViewModel: SongPlayerViewModel = viewModel()) {
    val playerUiState by playerViewModel.uiState.collectAsState()

    Row {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }
        IconButton(onClick = { playerViewModel.toggleSongPlay() }) {
            Icon(painter = painterResource(
                id = if (playerUiState.isPlaying) R.drawable.baseline_pause_circle_24 else R.drawable.baseline_play_circle_24
            ),
                contentDescription = "Play/Pause",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(47.dp)
            )
        }
    }
}

@Composable
fun ProgressBar(playerViewModel: SongPlayerViewModel = viewModel()) {
    val playerUiState by playerViewModel.uiState.collectAsState()

    // local slider value state
    var sliderValueRaw by remember { mutableStateOf(playerUiState.progression) }
    val sliderInteractionSource = remember { MutableInteractionSource() }
    val isDragged by sliderInteractionSource.collectIsDraggedAsState()
    if (isDragged) {
        playerViewModel.updateProgress(sliderValueRaw, SongPlayerViewModel.UpdateCause.USER)
    }

    Slider(
        value = playerUiState.progression,
        onValueChange = {
            sliderValueRaw = it
            playerViewModel.updateProgress(it, SongPlayerViewModel.UpdateCause.PROCESS)
        },
        valueRange = 0.0f..100.0f,
        interactionSource = sliderInteractionSource
    )
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
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = song.artists.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
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

fun initializePlayer(context: Context, song: Song, viewModel: SongPlayerViewModel) {
    val player = ExoPlayer.Builder(context).build()
    val mediaItem = MediaItem.fromUri(song.playbackUrl!!)
    player.setMediaItem(mediaItem)
    player.prepare()

    viewModel.updatePlayer(player)
    viewModel.updateSong(song)

    val handler = Handler()
    val runnable = object: Runnable {
        override fun run() {
            viewModel.updateProgress(
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
            if (playbackState == Player.STATE_ENDED && viewModel.uiState.value.isPlaying) {
                viewModel.toggleSongPlay()
            }
        }
    })
}
fun loadSongFromIntent(intent: Intent): Song {
    val songTitle = intent.getStringExtra("title").orEmpty()
    val songArtists = intent.getStringArrayListExtra("artists") as ArrayList<String>
    val imageUrl = intent.getStringExtra("imageURL").orEmpty()
    val playbackUrl = intent.getStringExtra("playbackURL").orEmpty()
    return Song(songTitle, songArtists, imageUrl, playbackUrl)
}