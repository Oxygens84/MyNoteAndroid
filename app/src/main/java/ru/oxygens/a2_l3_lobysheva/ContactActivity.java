package ru.oxygens.a2_l3_lobysheva;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by oxygens on 22/03/2018.
 */

public class ContactActivity extends AppCompatActivity
        implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Button ok = (Button) findViewById(R.id.button_ok);
        ok.setOnClickListener(this);

        Button map = (Button) findViewById(R.id.button_map);
        map.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_ok) {
            finish();
        }
        if (view.getId() == R.id.button_map) {
            String geo = "geo:55.734148,37.588747";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
            PackageManager packageManager = getPackageManager();
            if (isPackageInstalled("com.google.abdroid.apps.maps", packageManager)){
                intent.setPackage("com.google.abdroid.apps.maps");
            }
            startActivity(intent);
        }
    }

    private boolean isPackageInstalled (String packageName, PackageManager manager){
        try {
            manager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
