package com.jaydonga.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    static String NOTE_POSITION ="com.jwhh.notekeeper.NOTE_POSITION";
    private ArrayAdapter<NoteInfo> mAdapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(NoteListActivity.this,MainActivity.class));
            }
        });

        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapterNotes.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        final ListView listNotes = findViewById(R.id.list_note);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        mAdapterNotes = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,notes);
        listNotes.setAdapter(mAdapterNotes);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(NoteListActivity.this,MainActivity.class);
//                NoteInfo noteInfo = (NoteInfo) listNotes.getItemAtPosition(position);
//                intent.putExtra("NOTE_POSITION",noteInfo);
                intent.putExtra(NoteListActivity.NOTE_POSITION,position);
                startActivity(intent);
            }
        });
    }

}
