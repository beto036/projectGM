package com.example.admin.myexample;

import android.graphics.Bitmap;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.presenter.EditNotePresenter;
import com.example.admin.myexample.editnote.presenter.EditNotePresenterImpl;
import com.example.admin.myexample.editnote.view.EditNoteView;
import com.example.admin.myexample.rest.RetrofitHelper;
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by admin on 1/8/2017.
 */

public class EditNotePresenterTest {

    private EditNotePresenter editNotePresenter;

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
    private EditNoteView editNoteView;

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
    public void setUpEditNoteTest(){
        MockitoAnnotations.initMocks(this);
        editNotePresenter = new EditNotePresenterImpl(editNoteView, factory, storage);
    }

    @Test
    public void updateNoteTest(){
        byte[] data = null;
        Note note = new Note("My note", "This is test");
        Response<Note> response = Response.success(note);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        data = baos.toByteArray();
        when(factory.updateNote(note)).thenReturn(call);
        when(storage.getReferenceFromUrl(anyString())).thenReturn(storageReference);
        when(storageReference.child(anyString())).thenReturn(imageRef);
        when(imageRef.putBytes(data)).thenReturn(uploadTask);
        editNotePresenter.updateNote(note, imageBitmap);
        verify(editNoteView).showProgress();
        verify(editNoteView).hideErrors();
        verify(factory).updateNote(note);
        verify(call).enqueue(notesCallbackCaptor.capture());
        notesCallbackCaptor.getValue().onResponse(call, response);
        verify(imageRef).putBytes(data);
        verify(uploadTask).addOnSuccessListener(imageCallbackCaptor.capture());
        imageCallbackCaptor.getValue().onSuccess(taskSnapshot);
        verify(editNoteView).listNotes();
        verify(editNoteView).hideProgress();
    }

    @Test
    public void updateNoteTitleMissingTest(){
        Note note = new Note("", "This is test");
        editNotePresenter.updateNote(note, imageBitmap);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorTitle(anyString());
    }

    @Test
    public void updateNoteDescriptionMissingTest(){
        Note note = new Note("Test Note", "");
        editNotePresenter.updateNote(note, imageBitmap);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorDesc(anyString());
    }

    @Test
    public void updateNoteFieldsMissingTest(){
        Note note = new Note("", "");
        editNotePresenter.updateNote(note, imageBitmap);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorDesc(anyString());
        verify(editNoteView).showErrorTitle(anyString());
    }
}
