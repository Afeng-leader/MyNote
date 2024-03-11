package com.af.mynote.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.af.mynote.data.Note
import com.af.mynote.db.NotesAdapter
import com.af.mynote.db.NotesDatabase
import com.af.mynote.listener.NotesListener
import com.af.mynote.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NotesListener {
    private var TAG: String = MainActivity::class.java.name

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private var notesList = ArrayList<Note>()

    private val REQUEST_CODE_ADD_NOTE: Int = 1
    private val REQUEST_CODE_UPDATE_NOTE: Int = 2
    private val REQUEST_CODE_SHOW_NOTE: Int = 3
    private var noteClickedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val imageAddNoteMain: FloatingActionButton = findViewById(R.id.imageAddNoteMain)

        // 点击悬浮按钮，进入创建笔记界面
        imageAddNoteMain.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, CreateNoteActivity::class.java),
                REQUEST_CODE_ADD_NOTE
            )
        }

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView.setOnClickListener {

        }

        getNotes(REQUEST_CODE_SHOW_NOTE,false)

        //瀑布流，2列
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        notesRecyclerView.layoutManager = layoutManager
        notesAdapter = NotesAdapter(notesList,this)
        notesRecyclerView.adapter = notesAdapter

    }

    override fun onNoteClicked(note: Note, position: Int) {
        Log.d(TAG, "onNoteClicked: position: $position")
        noteClickedPosition = position
        val intent : Intent  = Intent(applicationContext, CreateNoteActivity::class.java)
        intent.putExtra("isViewOrUpdate",true)
        intent.putExtra("note",note)
        startActivityForResult(intent,REQUEST_CODE_UPDATE_NOTE)

    }



    //获取笔记列表
    private fun getNotes(requestCode : Int,isNoteDeleted: Boolean) {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask: AsyncTask<Void?,Void?,List<Note>>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg params: Void?): List<Note>? {

                return NotesDatabase.getDataBase(applicationContext)!!.noteDao()!!.getAllNotes()
            }

            // 刷新UI
            @SuppressLint("NotifyDataSetChanged")
            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: List<Note>) {
                super.onPostExecute(result)
                Log.d(TAG, "getNotes: onPostExecute : requestCode: $requestCode")

                when(requestCode) {
                    REQUEST_CODE_SHOW_NOTE -> {
                        notesList.addAll(result)
                        notesAdapter.notifyDataSetChanged()
                    }

                    REQUEST_CODE_ADD_NOTE -> {
                        notesList.add(0,result[0])
                        notesAdapter.notifyItemInserted(0)
                        notesRecyclerView.smoothScrollToPosition(0)
                    }

                    REQUEST_CODE_UPDATE_NOTE -> {
                        notesList.removeAt(noteClickedPosition)

                        if (isNoteDeleted) {
                            notesAdapter.notifyItemRemoved(noteClickedPosition)
                        } else {
                            notesList.add(noteClickedPosition, result[noteClickedPosition])
                            notesAdapter.notifyItemChanged(noteClickedPosition)
                        }
                    }

                }

            }

        }
        GetNotesTask().execute()
    }

    //更新List<Note>,update
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE,false)
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE,data.getBooleanExtra("isNoteDeleted",false))
            }
        }
    }
}