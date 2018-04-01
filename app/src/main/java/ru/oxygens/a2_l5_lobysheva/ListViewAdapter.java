package ru.oxygens.a2_l5_lobysheva;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oxygens on 16/03/2018.
 */

public class ListViewAdapter extends BaseAdapter {

    private final int SAMPLES_COUNT = 1;
    private final String SAMPLE_TITLE = "SAMPLE";
    private final String SAMPLE_TEXT = "Your text could be here :)";
    private final String internalFileName = "internal_file.lect";

    private List<Note> elements = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private String path;

    ListViewAdapter(Context context) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        path = context.getFilesDir() + "/" + internalFileName;
        readFromFile(path);
    }

    private void addSamples(int num) {
        for (int i = 0; i < num; i++) {
            elements.add(new Note(SAMPLE_TITLE + (i + 1), SAMPLE_TEXT));
        }
        saveToFile(path);
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

    void addElement(String title, String text) {
        elements.add(new Note(title, text));
        notifyDataSetChanged();
        saveToFile(path);
    }

    Note getItemInfo(int position) {
        return elements.get(position);
    }

    void updateElement(int position, String title, String text) {
        if (elements.size() > 0) {
            elements.get(position).setNoteTitle(title);
            elements.get(position).setNoteBody(text);
            notifyDataSetChanged();
            saveToFile(path);
        }
    }

    void deleteElement(int position) {
        if (elements.size() > 0) {
            elements.remove(position);
            notifyDataSetChanged();
            saveToFile(path);
        }
    }

    void deleteLastElement() {
        if (elements.size() > 0) {
            elements.remove(elements.size() - 1);
            notifyDataSetChanged();
            saveToFile(path);
        }
    }

    void deleteAll() {
        elements.clear();
        notifyDataSetChanged();
        saveToFile(path);
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        readFromFile(path);
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

        String text = elements.get(position).getNoteHeader();
        holder.textView.setText(text);

        return view;
    }

    class ListViewHolder {
        TextView textView;
    }


    private void saveToFile(String fileName) {
        File file;
        try {
            file = new File(fileName);

            FileOutputStream fileOutputStream;
            ObjectOutputStream objectOutputStream;

            if(!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(file, false);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(elements);
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void readFromFile(String fileName) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;

        try {
            fileInputStream = new FileInputStream(fileName);
            objectInputStream = new ObjectInputStream(fileInputStream);

            elements = (List<Note>)objectInputStream.readObject();

            objectInputStream.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
