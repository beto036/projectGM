package com.example.admin.myexample.rest.services;

import com.example.admin.myexample.data.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 1/7/2017.
 */

public interface NoteService {
    @GET("collections/notes")
    Call<List<Note>> getNotes(@Query("apiKey") String apiKey);

    @POST("collections/notes")
    Call<Note> saveNote(@Query("apiKey") String apiKey, @Body Note note);

    @PUT("collections/notes/{id}")
    Call<Note> updateNote(@Path("id") String id, @Query("apiKey") String apiKey, @Body Note note);
}
