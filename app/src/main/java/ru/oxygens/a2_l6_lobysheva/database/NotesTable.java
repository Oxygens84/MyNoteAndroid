package ru.oxygens.a2_l6_lobysheva.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by oxygens on 01/04/2018.
 */

public class NotesTable {

    private final static String TABLE_NAME = "notes";
    private final static String COLUMN_NOTE_ID = "_id";
    private final static String COLUMN_NOTE_TITLE = "n_title";
    private final static String COLUMN_NOTE_TEXT = "n_text";

    private static String[] notesAllColumn = {
            COLUMN_NOTE_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT
    };

    static void createTable(SQLiteDatabase database) {
        database.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NOTE_TITLE + " TEXT DEFAULT '[no_title]'," +
                        COLUMN_NOTE_TEXT + " TEXT);"
        );
    }

    static void onUpgrade(SQLiteDatabase database) {
        //database.execSQL("ALTER TABLE " + TABLE_NAME +
        //        " ADD COLUMN " + COLUMN_NOTE_TEXT + " TEXT;");
        database.execSQL("DROP TABLE " + TABLE_NAME + ";");
    }

    public static void addNote(int id, String title, String text, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_ID, id);
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE_TEXT, text);
        database.insert(TABLE_NAME, null, values);
    }

    public static void editNote(int note_id, String new_title, String new_text, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, new_title);
        values.put(COLUMN_NOTE_TEXT, new_text);
        database.update(TABLE_NAME, values, COLUMN_NOTE_ID + "=" + note_id, null);
    }

    public static void deleteNote(int note_id, SQLiteDatabase database) {
        database.delete(TABLE_NAME, COLUMN_NOTE_ID + " = " + note_id, null);
    }

    public static void deleteAll(SQLiteDatabase database) {
        database.delete(TABLE_NAME, null, null);
    }

    public static List<Integer> getAllNotes(SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, notesAllColumn, null, null, null, null, null);
        return getResultFromCursor(cursor);
    }

    public static List<String> getNoteTitle(int note_id, SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, notesAllColumn, COLUMN_NOTE_ID + " = " + note_id, null, null, null, null);
        return getResultFromCursor(cursor, COLUMN_NOTE_TITLE);
    }

    public static List<String> getNoteText(int note_id, SQLiteDatabase database) {
        Cursor cursor = database.query(TABLE_NAME, notesAllColumn, COLUMN_NOTE_ID + " = " + note_id, null, null, null, null);
        return getResultFromCursor(cursor, COLUMN_NOTE_TEXT);
    }

    public static List<Integer> getResultFromCursor(Cursor cursor) {
        List<Integer> result = null;

        if(cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            int noteIdIndex = cursor.getColumnIndex(COLUMN_NOTE_ID);

            int i = 0;
            do {
                result.add(cursor.getInt(noteIdIndex));
                i++;
            } while (cursor.moveToNext());
        }

        try {
            cursor.close();
        } catch (Exception ignored) {}
        return result == null ? new ArrayList<Integer>(0) : result;
    }

    public static List<String> getResultFromCursor(Cursor cursor, String column) {
        List<String> result = null;

        if(cursor != null && cursor.moveToFirst()) {
            result = new ArrayList<>(cursor.getCount());

            int noteIdIndex = cursor.getColumnIndex(column);

            int i = 0;
            do {
                result.add(cursor.getString(noteIdIndex));
                i++;
            } while (cursor.moveToNext());
        }

        try {
            cursor.close();
        } catch (Exception ignored) {}
        return result == null ? new ArrayList<String>(0) : result;
    }


}
