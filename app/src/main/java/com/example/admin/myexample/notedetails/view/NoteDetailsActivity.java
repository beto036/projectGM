package com.example.admin.myexample.notedetails.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.admin.myexample.R;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.view.EditNoteActivity;
import com.example.admin.myexample.notedetails.presenter.NoteDetailsPresenter;
import com.example.admin.myexample.notedetails.presenter.NoteDetailsPresenterImpl;
import com.example.admin.myexample.notes.helper.NotesAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NoteDetailsActivity extends AppCompatActivity implements NoteDetailsView{

    private Note note;
    private TextView titleTxt;
    private TextView descTxt;
    private TextView createdTxt;
    private TextView updatedTxt;
    private ImageView imageView;
    private NoteDetailsPresenter noteDetailsPresenter;


    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            imageView.setImageBitmap(resource);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        titleTxt = (TextView) findViewById(R.id.noteDetailsTitle);
        descTxt = (TextView) findViewById(R.id.noteDetailsDesc);
        createdTxt = (TextView) findViewById(R.id.noteDetailsCreated);
        updatedTxt = (TextView) findViewById(R.id.noteDetailsUpdated);
        imageView = (ImageView) findViewById(R.id.noteDetailsImage);
        noteDetailsPresenter = new NoteDetailsPresenterImpl(this);
        noteDetailsPresenter.showDetails();
    }

    @Override
    public void showDetails() {
        note = getIntent().getParcelableExtra(NotesAdapter.ViewHolder.MY_NOTE);
        titleTxt.setText(note.getTitle());
        descTxt.setText(note.getDescription());
        createdTxt.setText("Created On: " + note.getDateCreated());
        if(note.getDateUpdated() != null &&  !note.getDateUpdated().isEmpty())
            updatedTxt.setText("Last Update: " + note.getDateUpdated());
        if(note.getImage() != null &&  !note.getImage().isEmpty())
            downloadImage();
    }

    private void downloadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-4c17b.appspot.com");
        StorageReference imageRef = storageRef.child(note.getImage());
        Glide.with(this).using(new FirebaseImageLoader()).load(imageRef).asBitmap().into(target);
    }


    @Override
    public void goToEdit() {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(NotesAdapter.ViewHolder.MY_NOTE,note);
        startActivity(intent);
    }

    public void editNote(View view) {
        noteDetailsPresenter.editNote();
    }

}
