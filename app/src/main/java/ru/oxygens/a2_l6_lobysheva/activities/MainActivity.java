package ru.oxygens.a2_l6_lobysheva.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;

import ru.oxygens.a2_l6_lobysheva.RecyclerViewAdapter;
import ru.oxygens.a2_l6_lobysheva.R;
import ru.oxygens.a2_l6_lobysheva.WeatherDataLoader;
import ru.oxygens.a2_l6_lobysheva.database.DatabaseHelper;

/**
 * Created by oxygens on 16/03/2018.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String KEY_TITLE = "NOTE_TITLE";
    public static final String KEY_BODY = "NOTE_BODY";
    public static final String KEY_POSITION = "POSITION";
    public static final String KEY_MODE = "MODE";

    public static String bug_text = "";
    public static String email_subject = "";

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView noResultView;

    int code = 1;
    int selected_position = -1;

    private final Handler handler = new Handler();
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView cityTextView;
    private TextView detailsTextView;

    private LocationManager mLocManager = null;
    private LocationListener mLocListener = null;
    Location location = null;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bug_text = getString(R.string.bug_text);
        email_subject = getString(R.string.feedback_header);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initDB();
        initListView();
        initDrawer(toolbar);
        initNavigationView();
        initFB();
        registerForContextMenu(recyclerView);
        initWeatherView();
        getUserLocation();
        updateWeatherData(location);
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initListView() {
        recyclerView = findViewById(R.id.recyclerListView);
        noResultView = findViewById(R.id.no_result);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(database);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnCreateContextMenuListener(this);

        setEmptyViewMessage();
    }

    public void initNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initDB() {
        database = new DatabaseHelper(getApplicationContext()).getWritableDatabase();
    }

    private void initFB(){
        FloatingActionButton fab = findViewById(R.id.fab_create);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClick(R.id.action_create);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        selected_position = adapter.getPosition();
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        super.onContextMenuClosed(menu);
        selected_position = adapter.getPosition();
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
                break;
            }
            case R.id.action_update: {
                openForm(NoteActivity.mode_update);
                break;
            }
            case R.id.action_view: {
                openForm(NoteActivity.mode_view);
                break;
            }
            case R.id.action_delete: {
                adapter.deleteElement();
                break;
            }
            case R.id.action_delete_all: {
                adapter.deleteAll();
                break;
            }
            default: {
                return false;
            }
        }
        setEmptyViewMessage();
        return true;
    }

    private void openCreateForm() {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(KEY_POSITION, -1);
        startActivityForResult(intent, code);
    }

    private void openForm(String mode) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(KEY_POSITION, selected_position);
        if (selected_position > -1) {
            intent.putExtra(KEY_TITLE, adapter.getItemTitle(selected_position));
            intent.putExtra(KEY_BODY, adapter.getItemText(selected_position));
            intent.putExtra(KEY_MODE, mode);
        }
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent answerIntent) {
        super.onActivityResult(requestCode, resultCode, answerIntent);
        if (answerIntent == null) {
            return;
        }
        if (requestCode == code && resultCode == RESULT_OK) {
            String savedTitle = answerIntent.getStringExtra(KEY_TITLE);
            String savedBody = answerIntent.getStringExtra(KEY_BODY);
            int position = answerIntent.getIntExtra(KEY_POSITION, -1);
            if (position > -1) {
                adapter.updateElement(savedTitle, savedBody);
            } else {
                adapter.addElement(savedTitle, savedBody);
            }
        }
        setEmptyViewMessage();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_slideshow) {
            showToast(bug_text);
        } else if (id == R.id.nav_manage) {
            showToast(bug_text);
        } else if (id == R.id.nav_share) {
            showToast(bug_text);
        } else if (id == R.id.nav_contacts) {
            Intent intent = new Intent(this, ContactActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            sendFeedback();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendFeedback() {
        TextView email = findViewById(R.id.contact_email);

        String uriText =
                "mailto:" + email.getText().toString() +
                        "?subject=" + Uri.encode(email_subject);
        Uri uri = Uri.parse(uriText);

        Intent feedback = new Intent(Intent.ACTION_SENDTO);
        feedback.setData(uri);
        if (feedback.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(feedback, email_subject));
        }
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void initWeatherView() {
        mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        cityTextView = findViewById(R.id.city_field);
        detailsTextView = findViewById(R.id.details_field);
    }

    private void updateWeatherData(Location location) {
        if (location != null) {
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            new Thread() {
                @Override
                public void run() {
                    final JSONObject json = WeatherDataLoader.getJSONData(getApplicationContext(), lat, lon);
                    if (json == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.place_not_found, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(json);
                            }
                        });
                    }
                }
            }.start();
        } else {
            cityTextView.setText(bug_text);
            detailsTextView.setText(R.string.no_weather);
        }
    }

    private void renderWeather(JSONObject jsonObject) {
        Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            String cityText = jsonObject.getString("name").toUpperCase(Locale.US) + ", "
                    + jsonObject.getJSONObject("sys").getString("country");
            cityTextView.setText(cityText);

            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            String currentTempText = String.format("%.2f", main.getDouble("temp")) + " \u2103";
            String detailsText =
                    details.getString("description").toUpperCase(Locale.US) + ", "
                            + currentTempText + ", " + main.getString("humidity") + "%"
                            + ", " + main.getString("pressure") + " hPa";

            detailsTextView.setText(detailsText);

        } catch (Exception exc) {
            Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }


    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        } else {
            location = mLocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Log.e(LOG_TAG, "NETWORK_PROVIDER: " + location.getLongitude() + " " + location.getLatitude());
            } else {
                location = mLocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    Log.e(LOG_TAG, "GPS_PROVIDER: " + location.getLongitude() + " " + location.getLatitude());
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean permissionsGranted = false;
        if (requestCode == 100) {
            permissionsGranted = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED;
        }
        if (permissionsGranted) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocListener == null) mLocListener = new LocListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        } else {
            mLocManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 3000L, 1.0F, mLocListener);
            mLocManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 3000L, 1.0F, mLocListener);
        }
    }

    @Override
    protected void onPause() {
        if (mLocListener != null) {
            mLocManager.removeUpdates(mLocListener);
        }
        super.onPause();
    }

    private final class LocListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            updateWeatherData(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {  }
        @Override
        public void onProviderEnabled(String provider) {  }
        @Override
        public void onProviderDisabled(String provider) {  }
    }

    private void setEmptyViewMessage(){
        if (adapter.getItemCount() == 0) {
            noResultView.setVisibility(View.VISIBLE);
        }
        else {
            noResultView.setVisibility(View.GONE);
        }
    }

}