package com.denis1986.android.base.util.extensions

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by denis.druzhinin on 01.07.2019.
 */

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.afterTextChangedEditable {editable -> afterTextChanged(editable.toString())}
}

fun EditText.afterTextChangedEditable(afterTextChanged: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.setTextIfChanged(newValue: CharSequence?) {
    if (text.toString() != newValue) {
        setText(newValue)
    }
}

fun ImageView.setGrayscaleColor(setGrayScale: Boolean) {
    if (setGrayScale) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        this.colorFilter = ColorMatrixColorFilter(matrix)
    } else {
        this.colorFilter = null
    }
}

fun TextView.onActionDone(onDone: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE ->
                onDone()
        }
        false
    }
}

/**
 * Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
 * item added to the end of the list as an anchor during initial load.
 */
fun RecyclerView.restoreScrollPositionIfNeeded() {
    if (layoutManager is LinearLayoutManager) {
        val linearLayoutManager = (layoutManager as LinearLayoutManager)
        val position = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        if (position != RecyclerView.NO_POSITION) {
            scrollToPosition(position)
        }
    }
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (inputMethodManager is InputMethodManager) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
    if (inputMethodManager is InputMethodManager) {
        inputMethodManager.showSoftInput(this, 0)
    }
}