package com.example.notepad.activity

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.adapter.NoteAdapter
import com.example.notepad.adapter.RecyclerItemTouchHelper
import com.example.notepad.fragment.BottomNavigationDrawerFragment
import com.example.notepad.room.Note
import com.example.notepad.viewModel.NoteViewModel

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NoteAdapter.CustomOnItemClickListener, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private val ADD_NOTE_REQUEST = 1
    val EDIT_REQUEST_CODE = 2
    lateinit var activity: Activity
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    lateinit var undoNote: Note
    lateinit var notes: List<Note>

    override fun onStart() {
        super.onStart()
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this,
                Observer<List<Note>> { t ->
                    adapter.submitList(t)
                    notes = t
                    if (notes != null && notes.size == 0) {
                        recycler_view.visibility == View.GONE
                        no_notes.visibility = View.VISIBLE
                    } else {
                        no_notes.visibility = View.GONE
                        recycler_view.visibility == View.VISIBLE
                    }
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        adapter = NoteAdapter(this, this)

        buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("request", ADD_NOTE_REQUEST)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        bottomAppBar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.delete_all) {
                if (adapter.itemCount != 0) {

                    MaterialAlertDialogBuilder(this, R.style.RoundShapeTheme)
                            .setTitle("Delete All Notes")
                            .setMessage("Do you want to delete all items?")
                            .setPositiveButton("delete") { dialogInterface, i ->
                                noteViewModel.deleteAllNotes()
                                Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
                            }
                            .setNegativeButton("cancel") { dialogInterface, i ->
                                dialogInterface.dismiss()
                            }.show()

                } else {
                    Toast.makeText(this, "Nothing to delete!", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        bottomAppBar.setNavigationOnClickListener { view: View? ->
            val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
            bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this,
                Observer<List<Note>> { t ->
                    adapter.submitList(t)
                    notes = t
                })

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
                RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val newNote = data.getSerializableExtra("bundle") as Note
            noteViewModel.insert(newNote)
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()

        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val note = data.getSerializableExtra("bundle") as Note

            noteViewModel.updateNote(note)

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved!", Toast.LENGTH_SHORT).show()
        }
    }



    private fun displayMaterialSnackBar() {
        val marginSide = 0
        val marginBottom = 550

        val snackbar = Snackbar.make(coordinator, "deleted", Snackbar.LENGTH_LONG).setAction("UNDO") { noteViewModel.insert(undoNote) }

        // Changing message text color
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

        snackbar.anchorView = buttonAddNote
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams

        params.setMargins(params.leftMargin + marginSide,
                params.topMargin, params.rightMargin + marginSide,
                params.bottomMargin + marginBottom)
        snackbarView.layoutParams = params
        snackbar.show()

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is NoteAdapter.NoteHolder) {
            val position = viewHolder.adapterPosition
            undoNote = adapter.getNoteAtPosition(position)!!
            noteViewModel.deleteNote(adapter.getNoteAtPosition(position))
            displayMaterialSnackBar()
        }
    }

    override fun onItemClick(note: Note?, itemView: View?) {

        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("bundle", note)

        intent.putExtra("request", EDIT_REQUEST_CODE)
        val options = ActivityOptions.makeSceneTransitionAnimation(activity,
            Pair.create(itemView?.findViewById(R.id.text_view_title), this.getString(R.string.titletransition)),
            Pair.create(itemView?.findViewById(R.id.text_view_description), this.getString(R.string.desc_transition)
            )
        )
        startActivityForResult(intent, EDIT_REQUEST_CODE, options.toBundle())
    }
}