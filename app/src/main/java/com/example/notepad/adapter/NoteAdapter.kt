package com.example.notepad.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.R
import com.example.notepad.room.Note


class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder> {
    private var listener: CustomOnItemClickListener? = null
    private var context: Context? = null
    private var lastPosition = -1
    lateinit var background: Array<Drawable?>

    constructor(listener: CustomOnItemClickListener, context: Context) : super(DIFF_CALLBACK) {
        this.listener = listener
        this.context = context
        initBackground(context)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return (oldItem.description.equals(newItem.description)
                        && oldItem.title.equals(newItem.title))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = getItem(position)
        holder.textViewTitle.text = currentNote.title
        holder.textViewDescription.text = currentNote.description
        holder.parentLayout.background = getBackground()
    }

    inner class NoteHolder : RecyclerView.ViewHolder {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        var parentLayout: ConstraintLayout = itemView.findViewById(R.id.ll)

        constructor(itemView: View) : super(itemView) {

            itemView.setOnClickListener(
                object : View.OnClickListener {
                    override fun onClick(v: View) {
                        if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                            listener!!.onItemClick(getItem(getAdapterPosition()), itemView)
                        }
                    }
                })
        }
    }

    interface CustomOnItemClickListener {
        fun onItemClick(note: Note?, itemView: View?)
    }

    fun getNoteAtPosition(position: Int): Note? {
        return getItem(position)
    }

    private fun initBackground(context: Context) {
        background = arrayOf(
            context.getDrawable(R.drawable.item1_background),
            context.getDrawable(R.drawable.item2_background),
            context.getDrawable(R.drawable.item3_background),
            context.getDrawable(R.drawable.item4_background),
            context.getDrawable(R.drawable.item5_background)
        )
    }


    private fun getBackground(): Drawable? {
        lastPosition++
        if (lastPosition < background?.size!!) {
            return background?.get(lastPosition)
        }
        lastPosition = 0
        return background?.get(0)
    }
}