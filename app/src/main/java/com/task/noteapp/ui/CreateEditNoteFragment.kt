package com.task.noteapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.task.noteapp.R
import com.task.noteapp.database.NoteDataBase
import com.task.noteapp.entity.Note
import com.task.noteapp.utils.hideKeyboard
import kotlinx.android.synthetic.main.create_edit_note_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class CreateEditNoteFragment : Fragment() {

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
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {

                context?.let {
                    val note = NoteDataBase.getDataBase(it).noteDao().getNote(noteId)

                    etNoteTitle.setText(note.title)
                    etNoteDesc.setText(note.description)

                    note.imgUrl?.let { path ->
                        selectedImagePath = path
                        Glide.with(it)
                            .load(path)
                            .into(ivNoteImg);

                        layoutImage.visibility = View.VISIBLE
                    }

                    tvDateTime.text = note.date
                }
            }
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
                    selectedImagePath = getPathFromUri(selectedImageUrl)
                    Glide.with(requireContext())
                        .load(selectedImagePath)
                        .into(ivNoteImg);

                    layoutImage.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }


    private fun updateNote() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            context?.let {
                val note = NoteDataBase.getDataBase(it).noteDao().getNote(noteId)

                note.title = etNoteTitle.text.toString()
                note.description = etNoteDesc.text.toString()
                note.date = getCurrentDate()
                note.edited = true
                note.imgUrl = selectedImagePath

                NoteDataBase.getDataBase(it).noteDao().editNote(note)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
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
                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    val note = Note()
                    note.title = etNoteTitle?.text.toString()
                    note.description = etNoteDesc?.text.toString()
                    note.date = getCurrentDate()
                    note.imgUrl = selectedImagePath

                    context?.let {
                        NoteDataBase.getDataBase(it).noteDao().addNote(note)
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
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