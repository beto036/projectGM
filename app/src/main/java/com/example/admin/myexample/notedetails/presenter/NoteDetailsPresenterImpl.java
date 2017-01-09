package com.example.admin.myexample.notedetails.presenter;

import com.example.admin.myexample.notedetails.view.NoteDetailsView;

/**
 * Created by admin on 1/9/2017.
 */

public class NoteDetailsPresenterImpl implements NoteDetailsPresenter {

    private NoteDetailsView noteDetailsView;

    public NoteDetailsPresenterImpl(NoteDetailsView noteDetailsView) {
        this.noteDetailsView = noteDetailsView;
    }

    @Override
    public void editNote() {
        this.noteDetailsView.goToEdit();
    }

    @Override
    public void showDetails() {
        this.noteDetailsView.showDetails();
    }
}
