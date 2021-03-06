package com.example.admin.myexample.notes.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notes.helper.NotesAdapter;
import com.example.admin.myexample.notes.view.NotesView;
import com.example.admin.myexample.rest.RetrofitHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/7/2017.
 */

public class NotesPresenterImpl implements NotesPresenter{

    private static final String TAG = "NotesPresenterTAG_";
    private NotesView notesView;
    private RetrofitHelper.Factory factory;
    private List<Note> notes;

    public NotesPresenterImpl(NotesView notesView, RetrofitHelper.Factory factory) {
        this.notesView = notesView;
        this.factory = factory;
    }

    @Override
    public void loadNotes() {
        notesView.showProgress();
        Call<List<Note>> call = factory.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                notes = response.body();
                notesView.refreshData(notes);
                notesView.hideProgress();
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                notesView.hideProgress();
            }
        });
    }

    @Override
    public void addNote() {
        notesView.openAddNote();
    }

}
