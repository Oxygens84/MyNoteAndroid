package ru.oxygens.a2l1_lobysheva;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by oxygens on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    public static final String KEY_TITLE = "NOTE_TITLE";
    public static final String KEY_BODY = "NOTE_BODY";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_MODE = "MODE";

    ListViewAdapter adapter;
    ListView listView;

    int code = 1;
    int selected_position = -1;

    public ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (selected_position > -1) {
                onMenuClick(item.getItemId());
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initListView();
        initDrawer(toolbar);
        initNavigationView();
        registerForContextMenu(listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
                selected_position = position;
                startActionMode(modeCallBack);
                view.setSelected(true);
                return true;
            }
        });

    }

    private Toolbar initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initListView() {
        listView = findViewById(R.id.listView);
        adapter = new ListViewAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener(this);
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return onMenuClick(id);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        return onMenuClick(id);
    }

    private boolean onMenuClick(int i) {
        switch (i) {
            case R.id.action_create: {
                openCreateForm();
                return true;
            }
            case R.id.action_update: {
                openForm(NoteActivity.mode_update);
                return true;
            }
            case R.id.action_view: {
                openForm(NoteActivity.mode_view);
                return true;
            }
            case R.id.action_delete: {
                adapter.deleteElement(selected_position);
                return true;
            }
            case R.id.action_delete_last: {
                adapter.deleteLastElement();
                selected_position = -1;
                return true;
            }
            case R.id.action_delete_all: {
                adapter.deleteAll();
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private void openCreateForm() {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(KEY_POSITION, -1);
        startActivityForResult(intent, code);
    }

    private void openForm(String mode) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(KEY_POSITION, selected_position);
        if (selected_position > -1){
            intent.putExtra(KEY_TITLE, adapter.getItemInfo(selected_position).getNoteHeader());
            intent.putExtra(KEY_BODY, adapter.getItemInfo(selected_position).getNoteBody());
            intent.putExtra(KEY_MODE, mode);
        }
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent answerIntent) {
        super.onActivityResult(requestCode, resultCode, answerIntent);
        if (answerIntent == null) {return;}
        if(requestCode == code && resultCode == RESULT_OK) {
            String savedTitle = answerIntent.getStringExtra(KEY_TITLE);
            String savedBody = answerIntent.getStringExtra(KEY_BODY);
            int position = answerIntent.getIntExtra(KEY_POSITION, -1);
            if (position > -1){
                adapter.updateElement(position, savedTitle, savedBody);
            } else {
                adapter.addElement(savedTitle, savedBody);
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}