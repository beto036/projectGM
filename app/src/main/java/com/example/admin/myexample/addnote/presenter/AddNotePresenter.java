package com.example.admin.myexample.addnote.presenter;

import android.graphics.Bitmap;

import com.example.admin.myexample.data.Note;

/**
 * Created by admin on 1/7/2017.
 */

public interface AddNotePresenter {
    void saveNote(Note note, Bitmap imageBitmap);
    void takePicture();
}
