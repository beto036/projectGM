package com.example.admin.myexample.editnote.view;

/**
 * Created by admin on 1/7/2017.
 */

public interface EditNoteView {
    void showProgress();
    void hideProgress();
    void listNotes();
    void hideErrors();
    void showErrorDesc(String s);
    void showErrorTitle(String s);
}
