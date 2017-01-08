package com.example.admin.myexample;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.editnote.presenter.EditNotePresenter;
import com.example.admin.myexample.editnote.presenter.EditNotePresenterImpl;
import com.example.admin.myexample.editnote.view.EditNoteView;
import com.example.admin.myexample.rest.RetrofitHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private EditNoteView editNoteView;

    @Mock
    private RetrofitHelper.Factory factory;

    @Mock
    private Call<Note> call;

    @Captor
    private ArgumentCaptor<Callback<Note>> notesCallbackCaptor;

    @Before
    public void setUpEditNoteTest(){
        MockitoAnnotations.initMocks(this);
        editNotePresenter = new EditNotePresenterImpl(editNoteView, factory);
    }

    @Test
    public void updateNoteTest(){
        Note note = new Note("My note", "This is test");
        Response<Note> response = Response.success(note);
        when(factory.updateNote(note)).thenReturn(call);
        editNotePresenter.updateNote(note);
        verify(editNoteView).showProgress();
        verify(editNoteView).hideErrors();
        verify(factory).updateNote(note);
        verify(call).enqueue(notesCallbackCaptor.capture());
        notesCallbackCaptor.getValue().onResponse(call, response);
        verify(editNoteView).listNotes();
        verify(editNoteView).hideProgress();
    }

    @Test
    public void updateNoteTitleMissingTest(){
        Note note = new Note("", "This is test");
        editNotePresenter.updateNote(note);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorTitle(anyString());
    }

    @Test
    public void updateNoteDescriptionMissingTest(){
        Note note = new Note("Test Note", "");
        editNotePresenter.updateNote(note);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorDesc(anyString());
    }

    @Test
    public void updateNoteFieldsMissingTest(){
        Note note = new Note("", "");
        editNotePresenter.updateNote(note);
        verify(editNoteView).hideErrors();
        verify(editNoteView).showErrorDesc(anyString());
        verify(editNoteView).showErrorTitle(anyString());
    }
}
