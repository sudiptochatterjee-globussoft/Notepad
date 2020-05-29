package com.example.notepad.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class Note(var title: String, var description: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Ignore
    fun Note() {
    }
}