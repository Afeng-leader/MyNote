package com.af.mynote.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.af.mynote.data.Note

@Database(version = 1, entities = [Note::class])
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao?

    companion object {

        private var notesDatabase : NotesDatabase? =null

        @Synchronized
        fun getDataBase(context: Context?) : NotesDatabase? {

            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                    context!!,
                    NotesDatabase::class.java,
                    "noteds_db"
                ).build()
            }

            return notesDatabase
        }

    }
}