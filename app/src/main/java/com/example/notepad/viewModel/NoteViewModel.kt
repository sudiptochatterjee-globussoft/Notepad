package com.example.notepad.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.notepad.repository.NoteRepository
import com.example.notepad.room.Note

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: NoteRepository = NoteRepository(application)

    private var allNotes: LiveData<List<Note>> = repository.getAllNotes()

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun deleteNote(note: Note?) {
        repository.deleteData(note)
    }

    fun updateNote(note: Note?) {
        repository.updateData(note)
    }
}