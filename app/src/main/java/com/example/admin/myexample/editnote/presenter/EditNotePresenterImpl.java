package com.example.admin.myexample.editnote.presenter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.view.EditNoteView;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/7/2017.
 */

public class EditNotePresenterImpl implements EditNotePresenter {

    private EditNoteView editNoteView;
    private RetrofitHelper.Factory factory;
    private FirebaseStorage firebaseStorage;

    public EditNotePresenterImpl(EditNoteView editNoteView, RetrofitHelper.Factory factory, FirebaseStorage firebaseStorage) {
        this.editNoteView = editNoteView;
        this.factory = factory;
        this.firebaseStorage = firebaseStorage;
    }

    @Override
    public void updateNote(Note note, Bitmap imageBitmap) {
        final Bitmap bitmap = imageBitmap;
        final String image = note.getImage();
        if(validateFields(note)) {
            editNoteView.showProgress();
            Call<Note> call = factory.updateNote(note);
            call.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if(bitmap != null){
                        StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://myproject-4c17b.appspot.com");
                        StorageReference imageRef = storageRef.child(image);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = imageRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                editNoteView.hideProgress();
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                editNoteView.hideProgress();
                                editNoteView.listNotes();
                            }
                        });
                    }else{
                        editNoteView.hideProgress();
                        editNoteView.listNotes();
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    editNoteView.hideProgress();
                }
            });
        }
    }

    @Override
    public void changePicture() {
        editNoteView.changePicture();
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
