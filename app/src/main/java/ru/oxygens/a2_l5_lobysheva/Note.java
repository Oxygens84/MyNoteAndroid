package ru.oxygens.a2_l5_lobysheva;

import java.io.Serializable;

/**
 * Created by oxygens on 16/03/2018.
 */

class Note implements Serializable {

    private String noteTitle;
    private String noteBody;

    private final String TITLE_DEFAULT = "[no title]";
    private final String TEXT_DEFAULT = "";

    Note(String title, String text){
        setNoteTitle(title);
        setNoteBody(text);
    }

    String getNoteHeader(){
        return noteTitle;
    }

    String getNoteBody(){
        return noteBody;
    }

    void setNoteTitle(String title){
        if (title == null || "".equals(title)){
            noteTitle = TITLE_DEFAULT;
        } else {
            noteTitle = title;
        }
    }

    void setNoteBody(String text){
        if (text == null){
            noteBody = TEXT_DEFAULT;
        } else {
            noteBody = text;
        }
    }
}
