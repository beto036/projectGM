package com.example.admin.myexample;

import com.example.admin.myexample.notedetails.presenter.NoteDetailsPresenter;
import com.example.admin.myexample.notedetails.presenter.NoteDetailsPresenterImpl;
import com.example.admin.myexample.notedetails.view.NoteDetailsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by admin on 1/9/2017.
 */

public class NoteDetailsPresenterTest {

    private NoteDetailsPresenter noteDetailsPresenter;

    @Mock
    private NoteDetailsView noteDetailsView;

    @Before
    public void setUpTest(){
        MockitoAnnotations.initMocks(this);
        noteDetailsPresenter = new NoteDetailsPresenterImpl(noteDetailsView);
    }

    @Test
    public void showDetailsTest(){
        noteDetailsPresenter.showDetails();
        verify(noteDetailsView).showDetails();
    }

    @Test
    public void goEditNoteTest(){
        noteDetailsPresenter.editNote();
        verify(noteDetailsView).goToEdit();
    }

}
