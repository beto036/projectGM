package com.example.admin.myexample.addnote.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.myexample.addnote.view.AddNoteActivity;
import com.example.admin.myexample.addnote.view.AddNoteView;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/7/2017.
 */

public class AddNotePresenterImpl implements AddNotePresenter{

    private static final String TAG = "AddNoteTAG_";
    private AddNoteView addNoteView;
    private RetrofitHelper.Factory factory;
    private FirebaseStorage storage;

    public AddNotePresenterImpl(AddNoteView addNoteView, RetrofitHelper.Factory factory, FirebaseStorage firebaseStorage) {
        this.addNoteView = addNoteView;
        this.factory = factory;
        this.storage = firebaseStorage;
    }

    @Override
    public void saveNote(Note note, Bitmap imageBitmap) {
        final Bitmap bitmap = imageBitmap;
        final String image = note.getImage();
        if(validateFields(note)) {
            addNoteView.showProgress();
            Call<Note> call = factory.saveNote(note);
            call.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if(bitmap != null){
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-4c17b.appspot.com");
                        StorageReference imageRef = storageRef.child(image);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d(TAG, "onFailure: " + exception.toString());
                                addNoteView.hideProgress();
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                addNoteView.hideProgress();
                                addNoteView.listNotes();
                            }
                        });
                    }else{
                        addNoteView.hideProgress();
                        addNoteView.listNotes();
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    addNoteView.hideProgress();
                }
            });
        }
    }

    @Override
    public void takePicture() {
        addNoteView.takePicture();
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
