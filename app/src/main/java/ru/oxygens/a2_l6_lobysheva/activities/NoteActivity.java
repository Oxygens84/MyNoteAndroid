package ru.oxygens.a2_l6_lobysheva.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.oxygens.a2_l6_lobysheva.R;

/**
 * Created by oxygens on 17/03/2018.
 */

public class NoteActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static final String mode_view = "VIEW";
    public static final String mode_update = "UPDATE";
    int note_position;
    EditText title;
    EditText body;
    Intent answerIntent;
    String mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        note_position = getIntent().getIntExtra(MainActivity.KEY_POSITION, -1);

        if (getIntent().getStringExtra(MainActivity.KEY_MODE) != null){
            mode = getIntent().getStringExtra(MainActivity.KEY_MODE);
        } else {
            mode = mode_update;
        }

        title = findViewById(R.id.note_title);
        body = findViewById(R.id.note_body);

        title.setText(getIntent().getStringExtra(MainActivity.KEY_TITLE));
        body.setText(getIntent().getStringExtra(MainActivity.KEY_BODY));

        Button save = findViewById(R.id.button_save);
        Button cancel = findViewById(R.id.button_cancel);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (mode.equals(mode_view)){
            title.setEnabled(false);
            body.setEnabled(false);
            save.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        answerIntent = new Intent();
        if (view.getId() == R.id.button_save) {
            answerIntent.putExtra(MainActivity.KEY_TITLE, title.getText().toString());
            answerIntent.putExtra(MainActivity.KEY_BODY, body.getText().toString());
            answerIntent.putExtra(MainActivity.KEY_POSITION, note_position);
            setResult(RESULT_OK, answerIntent);
        }
        if (view.getId() == R.id.button_cancel) {
            setResult(RESULT_CANCELED, answerIntent);
        }
        finish();
    }

}
