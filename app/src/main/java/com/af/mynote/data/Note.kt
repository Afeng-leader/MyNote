package com.af.mynote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
class Note : Serializable {

    @PrimaryKey
    var id: Long = 0

    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "date_time")
    var dateTime: String? = null

    @ColumnInfo(name = "subtitle")
    var subTitle: String? = null

    @ColumnInfo(name = "note_text")
    var noteText: String? = null

    @ColumnInfo(name = "image_path")
    var imagePath: String? = null

    @ColumnInfo(name = "color")
    var color: String? = null

    @ColumnInfo(name = "voice_path")
    var voicePath: String? = null

    override fun toString(): String {
        return "Note(id=$id, title=$title, dateTime=$dateTime, subTitle=$subTitle, noteText=$noteText, imagePath=$imagePath, color=$color, voicePath=$voicePath)"
    }


}