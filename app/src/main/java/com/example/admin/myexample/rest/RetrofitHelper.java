package com.example.admin.myexample.rest;

import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.rest.services.NoteService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 11/6/16.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "https://api.mongolab.com/api/1/databases/notesdb/";
    public static final String API_KEY = "djZ5Olq7x94SzNkxDFI8rx-ZCV4ZZmU5";

    public static class Factory {
        private static final String TAG = "RetrofitHelperTAG_";

        public static Retrofit create() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().build())
                    .build();
        }

        public static Call<List<Note>> getNotes () {
            Retrofit retrofit = create();
            NoteService noteService = retrofit.create(NoteService.class);
            return noteService.getNotes(API_KEY);
        }

        public static Call<Note> saveNote(Note note){
            Retrofit retrofit = create();
            NoteService noteService = retrofit.create(NoteService.class);
            return noteService.saveNote(API_KEY,note);
        }

        public static Call<Note> updateNote(Note note){
            Retrofit retrofit = create();
            NoteService noteService = retrofit.create(NoteService.class);
            return  noteService.updateNote(note.getId().getOid(), API_KEY, note);
        }
    }

}
