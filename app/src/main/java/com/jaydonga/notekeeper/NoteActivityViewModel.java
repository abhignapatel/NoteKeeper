package com.jaydonga.notekeeper;

import android.os.Build;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    public static String ORIGINAL_NOTE_CORSE_ID = "com.jwhh.notekeeper.ORIGINAL_NOTE_CORSE_ID";
    public static String ORIGINAL_NOTE_TITLE= "com.jwhh.notekeeper.ORIGINAL_NOTE_TITLE";
    public static String ORIGINAL_NOTE_TEXT = "com.jwhh.notekeeper.ORIGINAL_NOTE_TEXT";


    public String mOriginalNoteCourseId;
    public String mOriginalNoteTitle;
    public String mOriginalNoteText;
    public boolean isNewlyCreated=true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_CORSE_ID,mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalNoteText);
    }
    public void restoreState(Bundle inState){
        ORIGINAL_NOTE_CORSE_ID = inState.getString(mOriginalNoteCourseId);
        ORIGINAL_NOTE_TITLE = inState.getString(mOriginalNoteTitle);
        ORIGINAL_NOTE_TEXT = inState.getString(mOriginalNoteText);
    }
}
