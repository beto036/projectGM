package com.example.admin.myexample;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notes.presenter.NotesPresenter;
import com.example.admin.myexample.notes.presenter.NotesPresenterImpl;
import com.example.admin.myexample.notes.view.NotesView;
import com.example.admin.myexample.rest.RetrofitHelper;
import com.example.admin.myexample.rest.services.NoteService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by admin on 1/8/2017.
 */

public class NotesPresenterTest {

    private NotesPresenter notesPresenter;

    @Mock
    private NotesView notesView;

    @Mock
    private RetrofitHelper.Factory retrofitFactory;

    @Mock
    private Call<List<Note>> call;

    @Captor
    private ArgumentCaptor<Callback<List<Note>>> notesCallbackCaptor;


    @Before
    public void setUpNotesPresenter(){
        MockitoAnnotations.initMocks(this);
        notesPresenter = new NotesPresenterImpl(notesView, retrofitFactory);
    }

    @Test
    public void clickOnAddNote(){
        notesPresenter.addNote();
        verify(notesView).openAddNote();
    }

    @Test
    public void loadNotes(){
        List<Note> notes = new ArrayList<Note>();
        Response<List<Note>> response = Response.success(notes);
        when(retrofitFactory.getNotes()).thenReturn(call);
        notesPresenter.loadNotes();
        verify(notesView).showProgress();
        verify(retrofitFactory).getNotes();
        verify(call).enqueue(notesCallbackCaptor.capture());
        notesCallbackCaptor.getValue().onResponse(call, response);
        verify(notesView).refreshData(response.body());
        verify(notesView).hideProgress();
    }

}
