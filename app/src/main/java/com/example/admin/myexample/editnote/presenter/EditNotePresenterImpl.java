package com.example.admin.myexample.editnote.presenter;

import com.example.admin.myexample.addnote.view.AddNoteView;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.view.EditNoteView;
import com.example.admin.myexample.rest.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/7/2017.
 */

public class EditNotePresenterImpl implements EditNotePresenter {

    private EditNoteView editNoteView;

    public EditNotePresenterImpl(EditNoteView editNoteView) {
        this.editNoteView = editNoteView;
    }

    @Override
    public void updateNote(Note note) {
        if(validateFields(note)) {
            editNoteView.showProgress();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            note.setDateUpdated(sdf.format(new Date()));
            Call<Note> call = RetrofitHelper.Factory.updateNote(note);
            call.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    editNoteView.hideProgress();
                    editNoteView.listNotes();
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    editNoteView.hideProgress();
                }
            });
        }
    }

    private boolean validateFields(Note note){
        boolean res = true;
        editNoteView.hideErrors();
        if(note.getTitle() == null || note.getTitle().isEmpty()) {
            editNoteView.showErrorTitle("This field cannot be empty");
            res = false;
        }
        if(note.getDescription() == null || note.getDescription().isEmpty()) {
            editNoteView.showErrorDesc("This field cannot be empty");
            res = false;
        }
        return res;
    }
}
