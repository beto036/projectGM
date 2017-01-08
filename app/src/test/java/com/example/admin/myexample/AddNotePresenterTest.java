package com.example.admin.myexample;

import com.example.admin.myexample.addnote.presenter.AddNotePresenter;
import com.example.admin.myexample.addnote.presenter.AddNotePresenterImpl;
import com.example.admin.myexample.addnote.view.AddNoteView;
import com.example.admin.myexample.data.Note;
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

public class AddNotePresenterTest {
    private AddNotePresenter addNotePresenter;

    @Mock
    private AddNoteView addNoteView;

    @Mock
    private RetrofitHelper.Factory factory;

    @Mock
    private Call<Note> call;

    @Captor
    private ArgumentCaptor<Callback<Note>> notesCallbackCaptor;

    @Before
    public void setUpAddNoteTest(){
        MockitoAnnotations.initMocks(this);
        this.addNotePresenter = new AddNotePresenterImpl(addNoteView,factory);
    }

    @Test
    public void addNoteTest(){
        Note note = new Note("My note", "This is test");
        Response<Note> response = Response.success(note);
        when(factory.saveNote(note)).thenReturn(call);
        addNotePresenter.saveNote(note);
        verify(addNoteView).showProgress();
        verify(addNoteView).hideErrors();
        verify(factory).saveNote(note);
        verify(call).enqueue(notesCallbackCaptor.capture());
        notesCallbackCaptor.getValue().onResponse(call, response);
        verify(addNoteView).listNotes();
        verify(addNoteView).hideProgress();
    }

    @Test
    public void addNoteTitleMissingTest(){
        Note note = new Note("", "This is test");
        addNotePresenter.saveNote(note);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorTitle(anyString());
    }

    @Test
    public void addNoteDescriptionMissingTest(){
        Note note = new Note("Test Note", "");
        addNotePresenter.saveNote(note);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorDesc(anyString());
    }

    @Test
    public void addNoteFieldsMissingTest(){
        Note note = new Note("", "");
        addNotePresenter.saveNote(note);
        verify(addNoteView).hideErrors();
        verify(addNoteView).showErrorDesc(anyString());
        verify(addNoteView).showErrorTitle(anyString());
    }
}
