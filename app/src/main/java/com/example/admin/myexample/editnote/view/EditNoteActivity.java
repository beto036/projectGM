package com.example.admin.myexample.editnote.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.admin.myexample.R;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.presenter.EditNotePresenter;
import com.example.admin.myexample.editnote.presenter.EditNotePresenterImpl;
import com.example.admin.myexample.notes.helper.NotesAdapter;
import com.example.admin.myexample.notes.view.NotesActivity;
import com.example.admin.myexample.rest.RetrofitHelper;

public class EditNoteActivity extends AppCompatActivity implements EditNoteView{

    private EditNotePresenter editNotePresenter;
    private Note note;
    private EditText titleEdit;
    private EditText descEdit;
    private TextInputLayout titleEditLayout;
    private TextInputLayout descEditLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editNotePresenter = new EditNotePresenterImpl(this, new RetrofitHelper.Factory());
        note = getIntent().getParcelableExtra(NotesAdapter.ViewHolder.MY_NOTE);
        progressBar = (ProgressBar) findViewById(R.id.notesProgressBar);
        titleEdit = (EditText) findViewById(R.id.editNoteTitle);
        descEdit = (EditText) findViewById(R.id.editNoteDesc);
        titleEditLayout = (TextInputLayout) findViewById(R.id.editNoteTitleLayout);
        descEditLayout = (TextInputLayout) findViewById(R.id.editNoteDescLayout);
        titleEdit.setText(note.getTitle());
        descEdit.setText(note.getDescription());
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
    public void listNotes() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideErrors() {
        titleEditLayout.setError("");
        descEditLayout.setError("");
    }

    @Override
    public void showErrorDesc(String error) {
        descEditLayout.setError(error);
    }

    @Override
    public void showErrorTitle(String error) {
        titleEditLayout.setError(error);
    }

    public void updateNote(View view) {
        note.setTitle(titleEdit.getText().toString());
        note.setDescription(descEdit.getText().toString());
        editNotePresenter.updateNote(note);
    }
}
