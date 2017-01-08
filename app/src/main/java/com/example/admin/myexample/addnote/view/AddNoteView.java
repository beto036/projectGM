package com.example.admin.myexample.addnote.view;

import com.example.admin.myexample.data.Note;

/**
 * Created by admin on 1/7/2017.
 */

public interface AddNoteView {
    void showProgress();
    void hideProgress();
    void showErrorTitle(String error);
    void showErrorDesc(String error);
    void hideErrors();
    void listNotes();
}
