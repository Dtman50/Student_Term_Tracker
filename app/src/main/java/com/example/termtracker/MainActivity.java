package com.example.termtracker;

import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startBtn = findViewById(R.id.startbtn);
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TermActivity.class);
            startActivity(intent);
        });
    }

}