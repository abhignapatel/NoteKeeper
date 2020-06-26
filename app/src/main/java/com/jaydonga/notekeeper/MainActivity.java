package com.jaydonga.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static String NOTE_POSITION ="com.jwhh.notekeeper.NOTE_POSITION";
    NoteInfo noteInfo;
    boolean isNewNote;
    private Spinner mSpinnercourses;
    private EditText mNoteTitle;
    private EditText mNoteText;
    private int mNotePosition;
    private boolean mIsCancelling;
   NoteActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider mViewModelProvider = new ViewModelProvider(getViewModelStore(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));

        mViewModel = mViewModelProvider.get(NoteActivityViewModel.class);

        if ( mViewModel.isNewlyCreated && savedInstanceState!=null)
            mViewModel.restoreState(savedInstanceState);

        mViewModel.isNewlyCreated = false;

        mNoteTitle =  findViewById(R.id.text_note_title);
        mNoteText = findViewById(R.id.text_note_text);

        mSpinnercourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        ArrayAdapter<CourseInfo> adapterCourses  =
            new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,courses);

        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnercourses.setAdapter(adapterCourses);

        readDisplayStateValue();
        saveOriginalValue();
        if (!isNewNote)
        displayNote(mSpinnercourses, mNoteTitle, mNoteText);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState!=null){
            mViewModel.saveState(outState);
        }
    }

    private void saveOriginalValue() {
        if (isNewNote)
            return;
        mViewModel.mOriginalNoteCourseId = noteInfo.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = noteInfo.getTitle();
        mViewModel.mOriginalNoteText = noteInfo.getText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCancelling){
            if (isNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
            }else {
                storePreviousNoteValues();
            }
        }else{
            saveNote();
        }

    }

    private void storePreviousNoteValues() {
        CourseInfo course  = DataManager.getInstance().getCourse(mViewModel.mOriginalNoteCourseId);
        noteInfo.setCourse(course);
        noteInfo.setTitle(mViewModel.mOriginalNoteTitle);
        noteInfo.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNote() {
        noteInfo.setCourse((CourseInfo) mSpinnercourses.getSelectedItem());
        noteInfo.setText(mNoteText.getText().toString());
        noteInfo.setTitle(mNoteTitle.getText().toString());
    }

    private void displayNote(Spinner spinnercourses, EditText noteTitle, EditText noteText) {

        List<CourseInfo>  courses = DataManager.getInstance().getCourses();
        int courseIndex = courses.indexOf(noteInfo.getCourse());
        spinnercourses.setSelection(courseIndex);
        noteText.setText(noteInfo.getText());
        noteTitle.setText(noteInfo.getTitle());
    }

    private void readDisplayStateValue() {
        Intent intent = getIntent();
//        noteInfo = intent.getParcelableExtra(NOTE_POSITION);
//        isNewNote = noteInfo = null;
        int position = intent.getIntExtra(NOTE_POSITION,-1);
        isNewNote = position == -1;
        if (isNewNote){
            createNewNote();
        }
        else{
            noteInfo = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        noteInfo = dm.getNotes().get(mNotePosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
            return true;
        }else if (id == R.id.action_cancel){
            mIsCancelling = true;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo courses = (CourseInfo) mSpinnercourses.getSelectedItem();
        String subject = mNoteTitle.getText().toString();
        String text = "checkout what I learned  in the pluralsight course"+
            courses.getTitle() +"\"\n "+mNoteText.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);
    }
}
