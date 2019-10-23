package com.example.conversioncalc;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private boolean isLengthConversion = true;
    private String fromSelection = "Yards";
    private String toSelection = "Meters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent information = getIntent();

        if (information.hasExtra("isLengthConversion")) {
            isLengthConversion = information.getBooleanExtra("isLengthConversion", true);
        }

        Spinner fromOptions = findViewById(R.id.fromOptions);
        Spinner toOptions = findViewById(R.id.toOptions);

        ArrayAdapter<CharSequence> lengthAdapter = ArrayAdapter.createFromResource(
                this, R.array.lengthOptions, android.R.layout.simple_spinner_item);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> volumeAdapter = ArrayAdapter.createFromResource(
                this, R.array.volumeOptions, android.R.layout.simple_spinner_item);
        volumeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (isLengthConversion) {
            fromOptions.setAdapter(lengthAdapter);
            toOptions.setAdapter(lengthAdapter);
        } else {
            fromOptions.setAdapter(volumeAdapter);
            toOptions.setAdapter(volumeAdapter);
        }

        fromOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromSelection = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("from", fromSelection);
                intent.putExtra("to", toSelection);
                setResult(CalculateActivity.SETTING, intent);
                finish();
            }
        });
    }
}
