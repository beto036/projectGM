package com.example.admin.myexample.notedetails.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.myexample.R;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.view.EditNoteActivity;
import com.example.admin.myexample.notes.helper.NotesAdapter;

public class NoteDetailsActivity extends AppCompatActivity implements NoteDetailsView{

    private Note note;
    private TextView titleTxt;
    private TextView descTxt;
    private TextView createdTxt;
    private TextView updatedTxt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleTxt = (TextView) findViewById(R.id.noteDetailsTitle);
        descTxt = (TextView) findViewById(R.id.noteDetailsDesc);
        createdTxt = (TextView) findViewById(R.id.noteDetailsCreated);
        updatedTxt = (TextView) findViewById(R.id.noteDetailsUpdated);
        showDetails();
    }

    @Override
    public void showDetails() {
        note = getIntent().getParcelableExtra(NotesAdapter.ViewHolder.MY_NOTE);
        titleTxt.setText(note.getTitle());
        descTxt.setText(note.getDescription());
        createdTxt.setText("Created On: " + note.getDateCreated());
        if(note.getDateUpdated() != null &&  !note.getDateUpdated().isEmpty())
            updatedTxt.setText("Last Update: " + note.getDateUpdated());
    }

    public void editNote(View view) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(NotesAdapter.ViewHolder.MY_NOTE,note);
        startActivity(intent);
    }
}
