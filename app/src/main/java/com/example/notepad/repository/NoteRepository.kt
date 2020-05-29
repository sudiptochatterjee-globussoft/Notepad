package com.example.notepad.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.notepad.room.Note
import com.example.notepad.room.NoteDao
import com.example.notepad.room.NoteDatabase

class NoteRepository(application: Application) {
    private var noteDao: NoteDao
    private var allNotes: LiveData<List<Note>>

    init {
        val database =
            NoteDatabase(
                application.applicationContext
            )
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNoteNotesAsyncTask(noteDao).execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun deleteData(note: Note?) {
        DeleteNoteNotesAsyncTask(noteDao).execute(note)
    }

    fun updateData(note: Note?) {
        UpdateNoteTask(noteDao).execute(note)
    }

    private class InsertNoteAsyncTask(noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
        val noteDao = noteDao

        override fun doInBackground(vararg params: Note?) {
            noteDao.insert(params[0]!!)
        }
    }

    private class DeleteAllNoteNotesAsyncTask(val noteDao: NoteDao) :
        AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg params: Unit?) {
            noteDao.deleteAllNotes()
        }
    }

    private class DeleteNoteNotesAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao.delete(params[0])
        }
    }

    private class UpdateNoteTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {
        override fun doInBackground(vararg params: Note?) {
            noteDao.updateData(params[0]!!)
        }
    }
}