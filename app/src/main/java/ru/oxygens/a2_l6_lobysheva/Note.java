package ru.oxygens.a2_l6_lobysheva;

import java.io.Serializable;

/**
 * Created by oxygens on 16/03/2018.
 */

class Note implements Serializable {

    private int noteId;
    private String noteTitle;
    private String noteBody;

    Note(int i, String title, String text){
        setNoteId(i);
        setNoteTitle(title);
        setNoteBody(text);
    }

    int getNoteId(){
        return noteId;
    }

    String getNoteHeader(){
        return noteTitle;
    }

    String getNoteBody(){
        return noteBody;
    }

    void setNoteId(int i){
        noteId = i;
    }

    void setNoteTitle(String title){
        noteTitle = title;
    }

    void setNoteBody(String text){
        noteBody = text;
    }
}
