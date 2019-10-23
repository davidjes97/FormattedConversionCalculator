package com.example.conversioncalc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculateActivity extends AppCompatActivity {

    public static final int SETTING = 1;

    //default conversion starts with length
    Boolean isLengthConversion = true;

    //default conversion values for each mode
    UnitsConverter.LengthUnits fromLength = UnitsConverter.LengthUnits.Yards;
    UnitsConverter.LengthUnits toLength = UnitsConverter.LengthUnits.Meters;
    UnitsConverter.VolumeUnits fromVolume = UnitsConverter.VolumeUnits.Gallons;
    UnitsConverter.VolumeUnits toVolume = UnitsConverter.VolumeUnits.Liters;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(CalculateActivity.this, SettingsActivity.class);
            intent.putExtra("isLengthConversion", isLengthConversion);
            startActivityForResult(intent, SETTING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == SETTING) {
            String fromUnit = data.getStringExtra("from");
            TextView fromUnits = findViewById(R.id.fromUnits);
            fromUnits.setText(fromUnit);

            String toUnit = data.getStringExtra("to");
            TextView toUnits = findViewById(R.id.toUnits);
            toUnits.setText(toUnit);

            if (isLengthConversion) {
                fromLength = UnitsConverter.LengthUnits.valueOf(fromUnit);
                toLength = UnitsConverter.LengthUnits.valueOf(toUnit);
            } else {
                fromVolume = UnitsConverter.VolumeUnits.valueOf(fromUnit);
                toVolume = UnitsConverter.VolumeUnits.valueOf(toUnit);
            }

            EditText fField = findViewById(R.id.fromField);
            fField.getText().clear();
            EditText tField = findViewById(R.id.toField);
            tField.getText().clear();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //title
        TextView titleLabel = findViewById(R.id.titleLabel);

        //text fields
        EditText fromField = findViewById(R.id.fromField);
        EditText toField = findViewById(R.id.toField);

        //unit labels
        TextView fromUnits = findViewById(R.id.fromUnits);
        TextView toUnits = findViewById(R.id.toUnits);

        //buttons
        Button calculatePressed = findViewById(R.id.calculateButton);
        Button clearPressed = findViewById(R.id.clearButton);
        Button modePressed = findViewById(R.id.modeButton);

        fromField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    fromField.getText().clear();
                    toField.getText().clear();
                }
            }
        });

        toField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if (isFocused) {
                    fromField.getText().clear();
                    toField.getText().clear();
                }
            }
        });

        modePressed.setOnClickListener(view -> {
            fromField.getText().clear();
            toField.getText().clear();

            if (isLengthConversion) {
                isLengthConversion = false;
                fromUnits.setText(fromVolume.toString());
                toUnits.setText(toVolume.toString());
                titleLabel.setText(R.string.titleVolume);
            } else {
                isLengthConversion = true;
                fromUnits.setText(fromLength.toString());
                toUnits.setText(toLength.toString());
                titleLabel.setText(R.string.titleLength);
            }

            stealFocusFromEditTexts(getCurrentFocus());
        });

        clearPressed.setOnClickListener(view -> {
            fromField.setText("");
            toField.setText("");
            stealFocusFromEditTexts(getCurrentFocus());
        });

        calculatePressed.setOnClickListener(view -> {
            double fromValue;
            double toValue;

            //converting in length, not volume
            if (isLengthConversion) {
                if (!fromField.getText().toString().equals("")) {
                    fromValue = Double.parseDouble(fromField.getText().toString());
                    toValue = UnitsConverter.convert(fromValue, fromLength, toLength);
                    toField.setText(String.valueOf(toValue));
                } else if (!toField.getText().toString().equals("")) {
                    toValue = Double.parseDouble(toField.getText().toString());
                    fromValue = UnitsConverter.convert(toValue, toLength, fromLength);
                    fromField.setText(String.valueOf(fromValue));
                }
            } else {
                if (!fromField.getText().toString().equals("")) {
                    fromValue = Double.parseDouble(fromField.getText().toString());
                    toValue = UnitsConverter.convert(fromValue, fromVolume, toVolume);
                    toField.setText(String.valueOf(toValue));
                } else if (!toField.getText().toString().equals("")) {
                    toValue = Double.parseDouble(toField.getText().toString());
                    fromValue = UnitsConverter.convert(toValue, toVolume, fromVolume);
                    fromField.setText(String.valueOf(fromValue));
                }
            }

            stealFocusFromEditTexts(getCurrentFocus());
        });
    }

    /**
     * Method used to toggle the soft keyboard when anywhere else on the screen is tapped.
     * This is called in the parent view of activity_main.xml as well as in this class.
     * Retrieved from: https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-
     * on-android-after-clicking-outside-edittext
     *
     * @param view view of the page
     */
    public void stealFocusFromEditTexts(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
