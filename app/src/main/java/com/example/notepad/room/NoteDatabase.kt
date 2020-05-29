package com.example.notepad.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            NoteDatabase::class.java, "notes_database")
            .addCallback(roomCallback)
            .allowMainThreadQueries()
            .build()


        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(
                    instance
                )
                    .execute()
            }
        }
    }
}

class PopulateDbAsyncTask(db: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
    private val noteDao = db?.noteDao()

    override fun doInBackground(vararg p0: Unit?) {
        noteDao?.insert(
            Note(
                "Title 1",
                "description 1"
            )
        )
        noteDao?.insert(
            Note(
                "Title 2",
                "description 2"
            )
        )
        noteDao?.insert(
            Note(
                "Title 3",
                "description 3"
            )
        )
        noteDao?.insert(
            Note(
                "Title 4",
                "description 4"
            )
        )
        noteDao?.insert(
            Note(
                "Title 5",
                "description 5"
            )
        )
        noteDao?.insert(
            Note(
                "Title 6",
                "description 6"
            )
        )
        noteDao?.insert(
            Note(
                "Title 7",
                "description 7"
            )
        )
    }
}