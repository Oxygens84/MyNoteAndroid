package ru.oxygens.a2_l5_lobysheva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.oxygens.a2_l5_lobysheva.database.NotesTable;

/**
 * Created by oxygens on 16/03/2018.
 */

public class ListViewAdapter extends BaseAdapter {

    private final String TITLE_DEFAULT = "[no title]";
    private final String TEXT_DEFAULT = "";

    private List<Integer> elements = new ArrayList<>();
    private LayoutInflater layoutInflater;

    private SQLiteDatabase database;

    ListViewAdapter(Context context, SQLiteDatabase database) {
        this.database = database;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (NotesTable.getAllNotes(database) != null) {
            elements = NotesTable.getAllNotes(database);
        }
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    String getItemTitle(int position) {
        int note_id = elements.get(position);
        return NotesTable.getNoteTitle(note_id, database).get(0);
    }


    String getItemText(int position) {
        int note_id = elements.get(position);
        return NotesTable.getNoteText(note_id, database).get(0);
    }

    void addElement(String title, String text) {
        int id = elements.size()+1;
        elements.add(id);
        if (title.isEmpty()){
            title = TITLE_DEFAULT;
        }
        NotesTable.addNote(id, title, text, database);
        notifyDataSetChanged();
    }

    void updateElement(int position, String title, String text) {
        if (elements.size() > 0) {
            int note_id = elements.get(position);
            if (title.isEmpty()){
                title = TITLE_DEFAULT;
            }
            NotesTable.editNote(note_id, title, text, database);
            notifyDataSetChanged();
        }
    }

    void deleteElement(int position) {
        if (elements.size() > 0) {
            int note_id = elements.get(position);
            NotesTable.deleteNote(note_id, database);
            elements.remove(position);
            notifyDataSetChanged();
        }
    }

    void deleteLastElement() {
        if (elements.size() > 0) {
            int position = elements.size()-1;
            deleteElement(position);
        }
    }

    void deleteAll() {
        NotesTable.deleteAll(database);
        elements.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ListViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_view_item, parent, false);
            holder = new ListViewHolder();
            holder.textView = (TextView) view.findViewById(R.id.element_header);

            holder.textView.setOnCreateContextMenuListener(new TextView.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    v.showContextMenu();
                }
            });

            view.setTag(holder);

        } else {
            holder = (ListViewHolder) view.getTag();
        }

        String text = getItemTitle(position);
        holder.textView.setText(text);

        return view;
    }

    class ListViewHolder {
        TextView textView;
    }


}
