package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.komsi.solve.Model.VersionModel;
import com.komsi.solve.Storage.SharedPrefManager;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout llProfiles, llCPass;
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        version = findViewById(R.id.version);
        VersionModel versionModel = SharedPrefManager.getInstance(SettingsActivity.this).versionModel();

        version.setText(versionModel.getVersion()+"."+versionModel.getSub_version());

        llProfiles = findViewById(R.id.llProfiles);
        llCPass = findViewById(R.id.llChangePassword);

        llProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangeProfile.class));
            }
        });

        llCPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
            }
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
