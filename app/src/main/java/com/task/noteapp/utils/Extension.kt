package com.task.noteapp.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = this.currentFocus
    if (currentFocusedView != null) {
        inputManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Context.getPathFromUri(contentUri: Uri): String? {
    val filePath: String?
    val cursor = this.contentResolver.query(contentUri, null, null, null, null)
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
