package com.task.noteapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.task.noteapp.R
import com.task.noteapp.entity.Note
import com.task.noteapp.utils.getPathFromUri
import com.task.noteapp.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.create_edit_note_fragment.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateEditNoteFragment : Fragment() {
    private val viewModel: NoteViewModel by viewModels()

    private var noteId: Int = -1
    private var selectedImagePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_edit_note_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgument()
        initViews()
        onClickListeners()
        setObservers()
    }

    private fun setObservers() {
        viewModel.noteData.observe(viewLifecycleOwner) { note ->
            etNoteTitle.setText(note.title)
            etNoteDesc.setText(note.description)

            note.imgUrl?.let { path ->
                context?.let {
                    selectedImagePath = path
                    Glide.with(it)
                        .load(path)
                        .into(ivNoteImg);

                    layoutImage.visibility = View.VISIBLE
                }
            }
            tvDateTime.text = note.date
        }
    }


    private fun getArgument() {
        arguments?.getInt(ARG_NOTE_ID)?.let {
            noteId = it
        }
    }

    private fun onClickListeners() {
        ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        ivDone.setOnClickListener {
            if (noteId != -1) {
                updateNote()
            } else {
                saveNote()
            }
        }

        ivDeleteImg.setOnClickListener {
            selectedImagePath = null
            layoutImage.visibility = View.GONE
        }

        ivUploadImg.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_CODE_IMAGE)
        }
    }

    private fun initViews() {
        if (noteId != -1) {
            viewModel.getNote(noteId)
        } else {
            tvDateTime.text = getCurrentDate()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        return sdf.format(Date())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    selectedImagePath = requireContext().getPathFromUri(selectedImageUrl)
                    Glide.with(requireContext())
                        .load(selectedImagePath)
                        .into(ivNoteImg);

                    layoutImage.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateNote() {
        val note = Note()
        note.title = etNoteTitle?.text.toString()
        note.description = etNoteDesc?.text.toString()
        note.date = getCurrentDate()
        note.edited = true

        viewModel.editNote(note)
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun saveNote() {
        when {
            etNoteTitle?.text.isNullOrEmpty() -> {
                Toast.makeText(context, getString(R.string.title_empty), Toast.LENGTH_SHORT).show()
            }
            etNoteDesc?.text.isNullOrEmpty() -> {
                Toast.makeText(context, getString(R.string.desc_empty), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                val note = Note()
                note.title = etNoteTitle?.text.toString()
                note.description = etNoteDesc?.text.toString()
                note.date = getCurrentDate()
                note.imgUrl = selectedImagePath

                viewModel.addNote(note)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().hideKeyboard()
    }

    companion object {
        private const val ARG_NOTE_ID = "ARG_NOTE_ID"
        private var REQUEST_CODE_IMAGE = 456

        fun newInstance(noteId: Int) = CreateEditNoteFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_NOTE_ID, noteId)
            }
        }
    }


}