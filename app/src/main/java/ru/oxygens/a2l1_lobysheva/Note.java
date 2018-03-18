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

    public String getNoteHeader(){
        return noteTitle;
    }

    public String getNoteBody(){
        return noteBody;
    }

    public Integer getNoteId(){
        return noteId;
    }

    public void setNoteTitle(String title){
        if (title == null || "".equals(title)){
            noteTitle = TITLE_DEFAULT;
        } else {
            noteTitle = title;
        }
    }

    public void setNoteBody(String text){
        if (text == null){
            noteBody = TEXT_DEFAULT;
        } else {
            noteBody = text;
        }
    }
}
