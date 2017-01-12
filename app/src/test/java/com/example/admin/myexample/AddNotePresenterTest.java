package com.example.admin.myexample;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.admin.myexample.addnote.presenter.AddNotePresenter;
import com.example.admin.myexample.addnote.presenter.AddNotePresenterImpl;
import com.example.admin.myexample.addnote.view.AddNoteView;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by admin on 1/8/2017.
 */

public class AddNotePresenterTest {
    private AddNotePresenter addNotePresenter;

    @Mock
    private FirebaseStorage storage;

    @Mock
    private StorageReference storageReference;

    @Mock
    private StorageReference imageRef;

    @Mock
    private UploadTask.TaskSnapshot taskSnapshot;

    @Mock
    private UploadTask uploadTask;

    @Mock
    private AddNoteView addNoteView;

    @Mock
    private RetrofitHelper.Factory factory;

    @Mock
    private Call<Note> call;

    @Mock
    private Bitmap imageBitmap;

    @Captor
    private ArgumentCaptor<Callback<Note>> notesCallbackCaptor;

    @Captor
    private ArgumentCaptor<OnSuccessListener> imageCallbackCaptor;


    @Before
    public void setUpAddNoteTest(){
        MockitoAnnotations.initMocks(this);
        this.addNotePresenter = new AddNotePresenterImpl(addNoteView,factory, storage);
    }

    @Test
    public void addNoteTest(){
        byte[] data = null;
        Note note = new Note("My note", "This is test");
        Response<Note> response = Response.success(note);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        when(factory.saveNote(note)).thenReturn(call);
        when(storage.getReferenceFromUrl(anyString())).thenReturn(storageReference);
        when(storageReference.child(anyString())).thenReturn(imageRef);
        when(imageRef.putBytes(data)).thenReturn(uploadTask);
        addNotePresenter.saveNote(note, imageBitmap);
        verify(addNoteView).showProgress();
        verify(addNoteView).hideErrors();
        verify(factory).saveNote(note);
        verify(call).enqueue(notesCallbackCaptor.capture());
        notesCallbackCaptor.getValue().onResponse(call, response);
        verify(imageRef).putBytes(data);
        verify(uploadTask).addOnSuccessListener(imageCallbackCaptor.capture());
        imageCallbackCaptor.getValue().onSuccess(taskSnapshot);
        verify(addNoteView).listNotes();
        verify(addNoteView).hideProgress();
    }

    @Test
    public void addNoteTitleMissingTest(){
        Note note = new Note("", "This is test");
        addNotePresenter.saveNote(note, imageBitmap);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorTitle(anyString());
    }

    @Test
    public void addNoteDescriptionMissingTest(){
        Note note = new Note("Test Note", "");
        addNotePresenter.saveNote(note, imageBitmap);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorDesc(anyString());
    }

    @Test
    public void addNoteFieldsMissingTest(){
        Note note = new Note("", "");
        addNotePresenter.saveNote(note, imageBitmap);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorDesc(anyString());
        verify(addNoteView).showErrorTitle(anyString());
    }
}
