package org.hyperskill.musicplayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.mainButtonSearch)
        searchButton.setOnClickListener {
            Toaster(getString(R.string.no_songs_found)).show(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.playlist_options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mainMenuAddPlaylist -> {
                Toaster(getString(R.string.no_songs_loaded)).show(this)
                true
            }
            R.id.mainMenuLoadPlaylist -> {
                SimpleDialog(getString(R.string.choose_playlist_to_load)).show(this)
                true
            }
            R.id.mainMenuDeletePlaylist -> {
                SimpleDialog(getString(R.string.choose_playlist_to_delete)).show(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}