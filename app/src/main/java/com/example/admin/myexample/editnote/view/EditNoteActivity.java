package com.example.admin.myexample.editnote.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.admin.myexample.R;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.presenter.EditNotePresenter;
import com.example.admin.myexample.editnote.presenter.EditNotePresenterImpl;
import com.example.admin.myexample.notes.helper.NotesAdapter;
import com.example.admin.myexample.notes.view.NotesActivity;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements EditNoteView{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String PICTURE = "picture";
    private static final String TAG = "EditNoteTAG_";
    private EditNotePresenter editNotePresenter;
    private Note note;
    private EditText titleEdit;
    private EditText descEdit;
    private TextInputLayout titleEditLayout;
    private TextInputLayout descEditLayout;
    private ProgressBar progressBar;
    private Bitmap imageBitmap;
    private ImageView imageView;


    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
            imageBitmap = resource;
            imageView.setImageBitmap(resource);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        editNotePresenter = new EditNotePresenterImpl(this, new RetrofitHelper.Factory(),FirebaseStorage.getInstance());
        note = getIntent().getParcelableExtra(NotesAdapter.ViewHolder.MY_NOTE);
        progressBar = (ProgressBar) findViewById(R.id.notesProgressBar);
        titleEdit = (EditText) findViewById(R.id.editNoteTitle);
        descEdit = (EditText) findViewById(R.id.editNoteDesc);
        imageView = (ImageView) findViewById(R.id.editNoteImage);
        titleEditLayout = (TextInputLayout) findViewById(R.id.editNoteTitleLayout);
        descEditLayout = (TextInputLayout) findViewById(R.id.editNoteDescLayout);
        titleEdit.setText(note.getTitle());
        descEdit.setText(note.getDescription());
        if (savedInstanceState != null) {
            imageBitmap = savedInstanceState.getParcelable(PICTURE);
            imageView.setImageBitmap(imageBitmap);
        }else{
            downloadImage();
        }
    }

    private void downloadImage() {
        if(note.getImage() != null && !note.getImage().isEmpty()) {
            Log.d(TAG, "downloadImage: " + note.getImage());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-4c17b.appspot.com");
            StorageReference imageRef = storageRef.child(note.getImage());
            Glide.with(this).using(new FirebaseImageLoader()).load(imageRef).asBitmap().into(target);
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

    @Override
    public void changePicture() {
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


    public void updateNote(View view) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmssS");
        note.setTitle(titleEdit.getText().toString());
        note.setDescription(descEdit.getText().toString());
        note.setDateUpdated(android.text.format.DateFormat.getDateFormat(this).format(date)
                + " " +  android.text.format.DateFormat.getTimeFormat(this).format(date));
        if (imageBitmap != null) note.setImage("Image" + sdf.format(date));
        editNotePresenter.updateNote(note, imageBitmap);
    }

    public void takePicture(View view) {
        editNotePresenter.changePicture();
    }
}
