package com.example.geoplay.Playlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.geoplay.RecommendedMusic.Song
import com.squareup.picasso.Picasso
import com.example.geoplay.R
import com.example.geoplay.SongPlayer.SongPlayer
import com.example.geoplay.SongPlayer.TrackedSongPlayerSongs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class SongListAdapter(context: Context, songList: List<SongFirebase>) : ArrayAdapter<SongFirebase>(context, 0, songList) {

    private class ViewHolder {
        lateinit var ivSongImage: ImageView
        lateinit var tvSongName: TextView
        lateinit var tvArtistName: TextView
        lateinit var btnRemoveSong: ImageView
    }

    private val songs = songList.toMutableList()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val viewHolder: ViewHolder

        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.song_item_row, parent, false)

            viewHolder = ViewHolder()
            viewHolder.ivSongImage = rowView.findViewById(R.id.ivSongImage)
            viewHolder.tvSongName = rowView.findViewById(R.id.tvSongName)
            viewHolder.tvArtistName = rowView.findViewById(R.id.tvArtistName)
            viewHolder.btnRemoveSong = rowView.findViewById(R.id.btnRemoveSong)

            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        val song = getItem(position)!!

        viewHolder.ivSongImage.setImageResource(R.drawable.signin_screen) //placholder
        Picasso.get().load(song.song.imageUrl).into(viewHolder.ivSongImage)
        viewHolder.tvSongName.text = song.song.title
        viewHolder.tvArtistName.text = song.song.artists[0]

        rowView?.setOnClickListener {
            val intent = Intent(context, SongPlayer::class.java)
            val tps = TrackedSongPlayerSongs(extractSongs(songs), position)
            intent.putExtra("sourceActivity", PlaylistActivity::class.java)
            intent.putExtra("trackedSongPlayerSongs", tps as Serializable)
            if (context is Activity) {
                (context as Activity).finish()
            }
            context.startActivity(intent)
        }

        viewHolder.btnRemoveSong.setOnClickListener {
            val db = Firebase.firestore
            val userUid = FirebaseAuth.getInstance().uid

            if (userUid == null) {
                Toast.makeText(context,"Kako biste mogli obaviti ovu radnju, morate se prijaviti ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val playlistDoc = db.collection("playlist").document(userUid)
            playlistDoc.get().addOnSuccessListener { docSnapshot ->
                val songs = docSnapshot.toObject(Playlist::class.java)?.songs
                val updatedSongs = songs?.filter { songId -> songId != song.ref}
                Firebase.firestore.runTransaction { transaction ->
                    transaction.update(playlistDoc, "songs", updatedSongs)
                    transaction.delete(db.collection("song").document(song.ref))
                }.addOnSuccessListener {
                    Toast.makeText(context, "Pjesma uspješno uklonjena iz albuma", Toast.LENGTH_SHORT).show()
                    (context as Activity).finish()
                }.addOnFailureListener {
                    Toast.makeText(context, "Dogodila se greška prilikom uklanjanja pjesme iz albuma", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return rowView!!
    }

    private fun extractSongs(firebaseSongs: MutableList<SongFirebase>): MutableList<Song> {
        val songs = mutableListOf<Song>()
        firebaseSongs.forEach{ fs ->
            songs.add(fs.song)
        }
        return songs
    }
}