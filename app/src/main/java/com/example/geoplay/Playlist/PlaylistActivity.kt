package com.example.geoplay.Playlist

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoplay.R
import com.example.geoplay.RecommendedMusic.Song
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlaylistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        window.decorView.setBackgroundColor(Color.parseColor("#FF1B1B1A"))
    }

    fun onBackButtonClick(view: View) { finish() }

    fun loadPlaylistSongs(callback: (MutableList<SongFirebase>?) -> Unit) {
        val db = Firebase.firestore
        val userUid = FirebaseAuth.getInstance().uid

        if (userUid == null) {
            Toast.makeText(this, "Albumi pjesama postoje samo za prijavljene korisnike", Toast.LENGTH_SHORT).show()
            callback(null)
            return
        }

        val songs: MutableSet<SongFirebase> = mutableSetOf()
        val playlistRef = db.collection("playlist").document(userUid)
        playlistRef.get()
            .addOnSuccessListener { playlistDocument ->
                val songDocList = playlistDocument.data?.get("songs") as? java.util.ArrayList<*>
                if (songDocList.isNullOrEmpty()) {
                    callback(songs.toMutableList())
                    return@addOnSuccessListener
                }

                var counter = 0
                for (songId in songDocList) {
                    val songIdString = songId.toString()
                    if (songIdString.isNotEmpty()) {
                        db.collection("song").document(songIdString).get()
                            .addOnSuccessListener { songDocument ->
                                if (songDocument.exists()) {
                                    val songData = songDocument.data
                                    if (songData != null) {
                                        songs.add(mapToSong(songData, songIdString))
                                    }
                                    counter++
                                    if (counter == songDocList.size) {
                                        callback(songs.toMutableList())
                                    }
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Dogodila se greška prilikom dohvaćanja pjesama iz albuma", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }


    private fun mapToSong(songData: MutableMap<String, Any>, songId: String): SongFirebase {
        var artists: java.util.ArrayList<String> = arrayListOf("")
        var playbackUrl: String = ""
        var imageUrl: String = ""
        var title: String = ""
        for((key,value) in songData) {
            if (key == "artists") {
                artists = songData[key] as java.util.ArrayList<String>
            } else if (key == "playbackUrl") {
                playbackUrl = songData[key] as String
            } else if (key == "imageUrl") {
                imageUrl = songData[key] as String
            } else if (key == "title") {
                title = songData[key] as String
            }
        }
        val song = Song(title, artists, imageUrl, playbackUrl)
        return SongFirebase(song, songId)
    }
}
