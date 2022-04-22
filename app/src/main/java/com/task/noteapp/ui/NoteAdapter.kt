package com.task.noteapp.ui

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.task.noteapp.R
import com.task.noteapp.entity.Note
import kotlinx.android.synthetic.main.item_add_new_note.view.*
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(
    private val onClickListener: (noteId: Int) -> Unit,
    private val deleteClickListener: (note: Note, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var noteList = arrayListOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ADD_NEW_NOTE) {
            AddNewNoteViewHolder(inflater.inflate(R.layout.item_add_new_note, parent, false))
        } else {
            AvailableNoteViewHolder(inflater.inflate(R.layout.item_note, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ADD_NEW_NOTE)
            (holder as AddNewNoteViewHolder).bind()
        else
            (holder as AvailableNoteViewHolder).bind(noteList[position], position)
    }

    override fun getItemCount(): Int = noteList.size

    override fun getItemViewType(position: Int): Int =
        if (position == 0) ADD_NEW_NOTE else AVAILABLE_NOTE

    inner class AddNewNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.cvAddNewNote.setOnClickListener {
                onClickListener(-1)
            }
        }
    }

    inner class AvailableNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note, position: Int) {
            itemView.tvTitle.text = note.title
            itemView.tvDesc.text = note.description
            itemView.tvDate.text = note.date.toString()
            itemView.tvEdited.isVisible = note.edited

            note.imgUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(itemView.ivImage)

                itemView.ivImage.visibility = View.VISIBLE
            }


            itemView.cvNote.setOnClickListener {
                onClickListener(note.id ?: -1)
            }

            itemView.ivDelete.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setMessage("Are you sure you want to delete note?")
                    .setPositiveButton(
                        android.R.string.yes
                    ) { _, _ ->
                        deleteClickListener(note, position)
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }

        }
    }

    fun deleteItem(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position);
    }

    companion object {
        const val ADD_NEW_NOTE = 1
        const val AVAILABLE_NOTE = 2
    }

}