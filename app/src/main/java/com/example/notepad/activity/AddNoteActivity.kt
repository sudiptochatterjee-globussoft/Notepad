package com.example.notepad.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad.R
import com.example.notepad.room.Note


import kotlinx.android.synthetic.main.activity_add_note.*

class AddNoteActivity : AppCompatActivity() {
    lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        supportActionBar?.title = "add note"

        var request = intent.getIntExtra("request", 1)
        println(request)

        if (request == 2) {
            supportActionBar?.title = "edit note"
            val intent = intent
            note = intent.getSerializableExtra("bundle") as Note
            edit_text_title.setText(note.title)
            edit_text_description.setText(note.description)
        } else {
            supportActionBar?.title = "add note"
            title = "Add Note"
            note = Note("", "")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.add_note -> {
                saveNote()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        if (edit_text_title.text.toString().trim().isBlank() || edit_text_description.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Can not insert empty note!", Toast.LENGTH_SHORT).show()
            return
        }
        note.title = edit_text_title.text.toString()
        note.description = edit_text_description.text.toString()

        val intent = Intent()
        intent.putExtra("bundle", note)

        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
