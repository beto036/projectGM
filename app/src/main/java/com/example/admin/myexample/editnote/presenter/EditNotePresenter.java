package com.example.admin.myexample.editnote.presenter;

import android.graphics.Bitmap;

import com.example.admin.myexample.data.Note;

/**
 * Created by admin on 1/7/2017.
 */

public interface EditNotePresenter {
    void updateNote(Note note, Bitmap imageBitmap);
    void changePicture();
}
