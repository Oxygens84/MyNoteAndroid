package ru.oxygens.a2l1_lobysheva;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oxygens on 16/03/2018.
 */

public class ListViewAdapter extends BaseAdapter {

    private final int SAMPLES_COUNT = 1;
    private final String SAMPLE_TITLE = "SAMPLE";
    private final String SAMPLE_TEXT = "Your text could be here :)";

    private List<Note> elements = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Integer counter = 0;
    private Context context;

    ListViewAdapter(Context context) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        addSamples(SAMPLES_COUNT);
    }

    private void addSamples(int num) {
        for (int i = 0; i < num; i++) {
            counter++;
            elements.add(new Note(counter, SAMPLE_TITLE + (i + 1), SAMPLE_TEXT));
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

    public Note getItemInfo(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void addElement(String title, String text) {
        counter++;
        elements.add(new Note(counter, title, text));
        notifyDataSetChanged();
    }


    void updateElement(int position, String title, String text) {
        if (elements.size() > 0) {
            elements.get(position).setNoteTitle(title);
            elements.get(position).setNoteBody(text);
            notifyDataSetChanged();
        }
    }

    void deleteElement(int position) {
        if (elements.size() > 0) {
            elements.remove(position);
            notifyDataSetChanged();
        }
    }

    void deleteLastElement() {
        if (elements.size() > 0) {
            elements.remove(elements.size() - 1);
            notifyDataSetChanged();
        }
    }

    void deleteAll() {
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

        String text = elements.get(position).getNoteHeader();
        holder.textView.setText(text);

        return view;
    }

    class ListViewHolder {
        protected TextView textView;
    }


}
