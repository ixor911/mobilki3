package com.example.mobilki3;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button  haffmanBtn = findViewById(R.id.mainHaffmanBtn),
                wheatBtn = findViewById(R.id.mainWheatBtn),
                contactsBtn = findViewById(R.id.mainContactsBtn),
                geoServiceBtn = findViewById(R.id.mainGeoServiceBtn),
                creditsBtn = findViewById(R.id.mainCreditsBtn),
                helpBtn = findViewById(R.id.mainHelpBtn);


        haffmanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HaffmanActivity.class);
                switchActivities(intent);
            }
        });

        wheatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WheatActivity.class);
                switchActivities(intent);
            }
        });

        contactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactsActivity.class);
                switchActivities(intent);
            }
        });

        geoServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeoServiceActivity.class);
                switchActivities(intent);
            }
        });

        creditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                switchActivities(intent);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                switchActivities(intent);
            }
        });
    }

    private void switchActivities(Intent intent) { startActivity(intent); }
}