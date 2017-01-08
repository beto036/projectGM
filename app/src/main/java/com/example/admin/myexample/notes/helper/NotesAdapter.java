package com.example.admin.myexample.notes.helper;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myexample.R;
import com.example.admin.myexample.data.Note;
import com.example.admin.myexample.notedetails.view.NoteDetailsActivity;

import java.util.List;

/**
 * Created by admin on 1/7/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private static final String TAG = "NotesAdapterTAG_";
    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View termView = inflater.inflate(R.layout.note_item, parent, false);

        return new ViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTxt.setText(note.getTitle());
        holder.descTxt.setText(note.getDescription());
        holder.note = note;

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public static final String MY_NOTE = "MY_NOTE";
        public TextView titleTxt;
        public TextView descTxt;
        public Note note;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTxt = (TextView) itemView.findViewById(R.id.noteTitle);
            descTxt = (TextView) itemView.findViewById(R.id.noteDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clock on item " + note.getTitle());
                    Intent intent = new Intent(v.getContext(), NoteDetailsActivity.class);
                    intent.putExtra(MY_NOTE, note);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
