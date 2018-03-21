package ru.oxygens.a2l1_lobysheva;

/**
 * Created by oxygens on 16/03/2018.
 */

public class Note {

    private Integer noteId;
    private String noteTitle;
    private String noteBody;

    private final String TITLE_DEFAULT = "[no title]";
    private final String TEXT_DEFAULT = "";

    Note(Integer id, String title, String text){
        noteId = id;
        setNoteTitle(title);
        setNoteBody(text);
    }

    String getNoteHeader(){
        return noteTitle;
    }

    String getNoteBody(){
        return noteBody;
    }

    Integer getNoteId(){
        return noteId;
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
