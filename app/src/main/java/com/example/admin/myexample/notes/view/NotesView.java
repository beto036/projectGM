package com.example.admin.myexample.notes.view;

import com.example.admin.myexample.data.Note;

import java.util.List;

/**
 * Created by admin on 1/7/2017.
 */

public interface NotesView {
    void showProgress();
    void hideProgress();
    void refreshData(List<Note> notes);
}
