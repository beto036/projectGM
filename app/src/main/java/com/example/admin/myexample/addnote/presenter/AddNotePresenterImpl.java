package com.example.admin.myexample.addnote.presenter;

import android.content.Intent;

import com.example.admin.myexample.addnote.view.AddNoteView;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.rest.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/7/2017.
 */

public class AddNotePresenterImpl implements AddNotePresenter{

    private AddNoteView addNoteView;
    private RetrofitHelper.Factory factory;

    public AddNotePresenterImpl(AddNoteView addNoteView, RetrofitHelper.Factory factory) {
        this.addNoteView = addNoteView;
        this.factory = factory;
    }

    @Override
    public void saveNote(Note note) {
        if(validateFields(note)) {
            addNoteView.showProgress();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            note.setDateCreated(sdf.format(new Date()));
            Call<Note> call = factory.saveNote(note);
            call.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    addNoteView.hideProgress();
                    addNoteView.listNotes();
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    addNoteView.hideProgress();
                }
            });
        }
    }

    private boolean validateFields(Note note){
        boolean res = true;
        addNoteView.hideErrors();
        if(note.getTitle() == null || note.getTitle().isEmpty()) {
            addNoteView.showErrorTitle("This field cannot be empty");
            res = false;
        }
        if(note.getDescription() == null || note.getDescription().isEmpty()) {
            addNoteView.showErrorDesc("This field cannot be empty");
            res = false;
        }
        return res;
    }

}
