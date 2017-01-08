package com.example.admin.myexample.addnote.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.admin.myexample.R;
import com.example.admin.myexample.addnote.presenter.AddNotePresenter;
import com.example.admin.myexample.addnote.presenter.AddNotePresenterImpl;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notes.view.NotesActivity;
import com.example.admin.myexample.rest.RetrofitHelper;

public class AddNoteActivity extends AppCompatActivity implements AddNoteView{

    private AddNotePresenter addNotePresenter;
    private EditText titleEdit;
    private EditText descEdit;
    private TextInputLayout titleEditLayout;
    private TextInputLayout descEditLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        addNotePresenter = new AddNotePresenterImpl(this, new RetrofitHelper.Factory());
        titleEdit = (EditText) findViewById(R.id.addNoteTitle);
        descEdit = (EditText) findViewById(R.id.addNoteDesc);
        titleEditLayout = (TextInputLayout) findViewById(R.id.addNoteTitleLayout);
        descEditLayout = (TextInputLayout) findViewById(R.id.addNoteDescLayout);
        progressBar = (ProgressBar) findViewById(R.id.notesProgressBar);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        titleEdit.setEnabled(false);
        descEdit.setEnabled(false);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorTitle(String error) {
        titleEditLayout.setError(error);
    }

    @Override
    public void showErrorDesc(String error) {
        descEditLayout.setError(error);
    }

    @Override
    public void hideErrors() {
        titleEditLayout.setError("");
        descEditLayout.setError("");
    }

    @Override
    public void listNotes() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }

    public void saveNote(View view) {
        Note note = new Note(titleEdit.getText().toString(), descEdit.getText().toString());
        addNotePresenter.saveNote(note);
    }
}
