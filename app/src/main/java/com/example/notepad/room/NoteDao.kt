package com.example.notepad.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Query("DELETE FROM notes_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM notes_table order by id asc")
    fun getAllNotes(): LiveData<List<Note>>

    @Delete
    fun delete(note: Note?)

    @Update
    fun updateData(note: Note?)

}