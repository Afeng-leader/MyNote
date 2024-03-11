package com.af.mynote.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.af.mynote.data.Note
import com.af.mynote.db.NotesDatabase
import com.af.mynote.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNoteActivity : AppCompatActivity() {
    private var inputNoteTitle: EditText? = null
    private var inputNoteSubtitle: EditText? = null
    private var inputNoteText: EditText? = null
    private var textDateTime: TextView? = null
    private var viewSubtitleIndicator: View? = null


    private var alreadyAvailableNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_note)

        val imageBack: ImageView = findViewById(R.id.imageBack)
        // 返回上一级界面
        imageBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        inputNoteTitle = findViewById(R.id.inputNoteTitle)
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle)
        inputNoteText = findViewById(R.id.inputNote)
        textDateTime = findViewById(R.id.textDateTime)
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator)

        // 显示当前时间
        textDateTime?.text =
            SimpleDateFormat("yyyy年MM月dd日 EEEE a HH:mm", Locale.CHINA).format(Date())

        //保存笔记按钮
        val imageSave: ImageView = findViewById(R.id.imageSave)
        imageSave.setOnClickListener { saveNote() }

        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = intent.getSerializableExtra("note") as Note
            setViewOrUpdateNote()
        }

    }

    private fun setViewOrUpdateNote() {
        inputNoteTitle?.setText(alreadyAvailableNote?.title)
        inputNoteSubtitle?.setText(alreadyAvailableNote?.subTitle)
        inputNoteText?.setText(alreadyAvailableNote?.noteText)
        textDateTime?.text = alreadyAvailableNote?.dateTime
    }

    // 保存笔记
    private fun saveNote() {
        //标题和内容为空时，弹出Toast提醒，并返回空
        if (inputNoteTitle?.text.toString().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            return
        } else if (inputNoteSubtitle?.text.toString().isEmpty() && inputNoteText?.text.toString()
                .isEmpty()
        ) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        //新建一个note实体，并将笔记内容保存至note
        val note = Note()
        note.title = inputNoteTitle?.text.toString()
        note.subTitle = inputNoteSubtitle?.text.toString()
        note.noteText = inputNoteText?.text.toString()
        note.dateTime = textDateTime?.text.toString()

        if (alreadyAvailableNote != null) {
            note.id = alreadyAvailableNote?.id!!
        }
        // 将note 实体插入NoteDatbase中
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask : AsyncTask<Void?, Void?, Void?>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg params: Void?): Void? {
                NotesDatabase.getDataBase(applicationContext)!!.noteDao()!!.insertNote(note)
                return null
            }

            @Deprecated("Deprecated in Java")
            override fun onPreExecute() {
                super.onPreExecute()
                val intent = Intent()
                setResult(RESULT_OK,intent)
                finish()
            }

        }

        SaveNoteTask().execute()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {

        }
    }
}