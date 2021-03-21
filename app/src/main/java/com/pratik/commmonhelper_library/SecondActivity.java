package com.pratik.commmonhelper_library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null) {
            String toastMessage = extraBundle.getString("value");
            int value = extraBundle.getInt("value1");
            Toast.makeText(this, toastMessage + " Value - " + value, Toast.LENGTH_SHORT).show();
        }
    }
}