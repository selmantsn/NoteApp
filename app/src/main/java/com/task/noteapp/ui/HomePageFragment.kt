package com.task.noteapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.task.noteapp.R
import com.task.noteapp.entity.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_page.*

@AndroidEntryPoint
class HomePageFragment : BaseFragment() {

    private val viewModel: NoteViewModel by viewModels()

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

        setObservers()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            context?.let {

                val note: ArrayList<Note> = arrayListOf()
                note.add(Note())
            }
        }

    }

    private fun setObservers() {
        viewModel.getAllNotes()
        viewModel.allNoteListData.observe(viewLifecycleOwner) { noteList ->
            val notes: ArrayList<Note> = arrayListOf()
            notes.add(Note())
            notes.addAll(noteList)
            noteAdapter.setData(notes)
            rvNotes.adapter = noteAdapter
        }
    }

    private fun deleteNote(note: Note, position: Int) {
        viewModel.deleteNote(note)
        noteAdapter.deleteItem(position)
    }

    private fun initViews() {
        rvNotes.setHasFixedSize(true)
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rvNotes.layoutManager = gridLayoutManager
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }

}