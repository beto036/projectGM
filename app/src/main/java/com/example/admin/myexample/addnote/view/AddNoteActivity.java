package com.example.admin.myexample.addnote.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.admin.myexample.R;
import com.example.admin.myexample.addnote.presenter.AddNotePresenter;
import com.example.admin.myexample.addnote.presenter.AddNotePresenterImpl;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notes.view.NotesActivity;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity implements AddNoteView {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String PICTURE = "picture";
    private static final String TAG = "AddNoteTAG_";
    private AddNotePresenter addNotePresenter;
    private EditText titleEdit;
    private EditText descEdit;
    private ImageView imageView;
    private TextInputLayout titleEditLayout;
    private TextInputLayout descEditLayout;
    private ProgressBar progressBar;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        addNotePresenter = new AddNotePresenterImpl(this, new RetrofitHelper.Factory(), FirebaseStorage.getInstance());
        titleEdit = (EditText) findViewById(R.id.addNoteTitle);
        descEdit = (EditText) findViewById(R.id.addNoteDesc);
        imageView = (ImageView) findViewById(R.id.addNoteImage);
        titleEditLayout = (TextInputLayout) findViewById(R.id.addNoteTitleLayout);
        descEditLayout = (TextInputLayout) findViewById(R.id.addNoteDescLayout);
        progressBar = (ProgressBar) findViewById(R.id.notesProgressBar);
        if (savedInstanceState != null) {
            imageBitmap = savedInstanceState.getParcelable(PICTURE);
            imageView.setImageBitmap(imageBitmap);
        }
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

    @Override
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PICTURE, imageBitmap);
    }

    public void saveNote(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmssS");
        Date date = new Date();
        Note note = new Note(titleEdit.getText().toString(), descEdit.getText().toString());
        note.setDateCreated(android.text.format.DateFormat.getDateFormat(this).format(date)
                + " " +  android.text.format.DateFormat.getTimeFormat(this).format(date));
        if (imageBitmap != null) note.setImage("Image" + sdf.format(new Date()));
        addNotePresenter.saveNote(note, imageBitmap);
    }

    public void takePicture(View view) {
        addNotePresenter.takePicture();
    }

}
