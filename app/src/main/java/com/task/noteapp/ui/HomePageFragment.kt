package com.task.noteapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.task.noteapp.R
import com.task.noteapp.database.NoteDataBase
import com.task.noteapp.entity.Note
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomePageFragment : BaseFragment() {

    private val noteAdapter by lazy {
        NoteAdapter(
            onClickListener = {
                replaceFragment(
                    R.id.container,
                    CreateEditNoteFragment.newInstance(it)
                )
            },
            deleteClickListener = { note, position -> deleteNote(note, position) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            context?.let {

                val note: ArrayList<Note> = arrayListOf()
                note.add(Note())
                note.addAll(NoteDataBase.getDataBase(it).noteDao().getAllNote() as ArrayList<Note>)
                noteAdapter.noteList = note
                rvNotes.adapter = noteAdapter
            }
        }

    }

    private fun deleteNote(note: Note, position: Int) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            context?.let {
                NoteDataBase.getDataBase(it).noteDao().deleteNote(note)
                noteAdapter.deleteItem(position)
            }
        }
    }

    private fun initViews() {
        rvNotes.setHasFixedSize(true)
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvNotes.layoutManager = gridLayoutManager
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }

}