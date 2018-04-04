package ru.oxygens.a2_l6_lobysheva;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.oxygens.a2_l6_lobysheva.database.NotesTable;

/**
 * Created by oxygens on 16/03/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final String TITLE_DEFAULT = "[no title]";
    private List<Integer> elements = new ArrayList<>();
    private SQLiteDatabase database;

    private int selected_position;

    public RecyclerViewAdapter(SQLiteDatabase database) {
        this.database = database;
        if (NotesTable.getAllNotes(database) != null) {
            elements = NotesTable.getAllNotes(database);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String element = getItemTitle(position);
        holder.textView.setText(element);
        holder.textView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_position = position;
                v.showContextMenu();
            }
        });
    }


    @Override
    public int getItemCount() {
        return elements.size();
    }

    public int getPosition() {
        return selected_position;
    }


    public String getItemTitle(int position) {
        int note_id = elements.get(position);
        return NotesTable.getNoteTitle(note_id, database).get(0);
    }


    public String getItemText(int position) {
        int note_id = elements.get(position);
        return NotesTable.getNoteText(note_id, database).get(0);
    }

    public void addElement(String title, String text) {
        int id = elements.size()+1;
        elements.add(id);
        if (title.isEmpty()){
            title = TITLE_DEFAULT;
        }
        NotesTable.addNote(id, title, text, database);
        notifyItemInserted(elements.size());
    }

    public void updateElement(String title, String text) {
        if (elements.size() > 0) {
            int note_id = elements.get(selected_position);
            if (title.isEmpty()){
                title = TITLE_DEFAULT;
            }
            NotesTable.editNote(note_id, title, text, database);
            notifyItemChanged(selected_position);
        }
    }

    public void deleteElement() {
        if (elements.size() > 0) {
            int note_id = elements.get(selected_position);
            NotesTable.deleteNote(note_id, database);
            elements.remove(selected_position);
            notifyItemRemoved(selected_position);
        }
    }

    public void deleteLastElement() {
        if (elements.size() > 0) {
            selected_position = elements.size()-1;
            deleteElement();
        }
    }

    public void deleteAll() {
        NotesTable.deleteAll(database);
        elements.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.element_header);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.actions_title);
        }
    }


}
