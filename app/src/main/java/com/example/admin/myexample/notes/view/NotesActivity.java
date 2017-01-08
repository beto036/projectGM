package com.example.admin.myexample.notes.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.admin.myexample.R;
import com.example.admin.myexample.addnote.view.AddNoteActivity;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notes.helper.NotesAdapter;
import com.example.admin.myexample.notes.presenter.NotesPresenter;
import com.example.admin.myexample.notes.presenter.NotesPresenterImpl;
import com.example.admin.myexample.rest.RetrofitHelper;

import java.util.List;

public class NotesActivity extends AppCompatActivity implements NotesView{

    private static final String TAG = "NotesActivityTAG_";
    private NotesPresenter notesPresenter;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.notesRecycler);
        progressBar = (ProgressBar) findViewById(R.id.notesProgressBar);
        notesPresenter = new NotesPresenterImpl(this, new RetrofitHelper.Factory());
        loadNotes();
    }

    private void loadNotes() {
        notesPresenter.loadNotes();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void refreshData(List<Note> notes) {
        int numColumns = this.getResources().getInteger(R.integer.num_columns);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),numColumns));
        notesAdapter = new NotesAdapter(notes);
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void openAddNote() {
        Log.d(TAG, "addNote: Adding a note");
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    public void addNote(View view) {
        notesPresenter.addNote();
    }
}
