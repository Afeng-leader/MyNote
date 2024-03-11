package com.af.mynote.listener

import com.af.mynote.data.Note

interface NotesListener {

    fun onNoteClicked(note: Note, position: Int)
}