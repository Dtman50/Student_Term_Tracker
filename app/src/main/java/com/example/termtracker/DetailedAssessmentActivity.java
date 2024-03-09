package com.example.termtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailedAssessmentActivity extends AppCompatActivity {

    TextView assessmentTitle;
    TextView assessmentStartDate;
    TextView assessmentEndDate;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TextView assessmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_assessment);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Detailed Assessment View");
        }

        Button saveBtn = findViewById(R.id.assessment_save);
        Button assessmentStartNotification = findViewById(R.id.assessmentStart_notification);
        Button assessmentEndNotification = findViewById(R.id.assessmentEnd_notification);

        saveBtn.setOnClickListener((v -> {
            finish();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }));


        assessmentTitle = findViewById(R.id.detailed_assessmentTitle);
        assessmentStartDate = findViewById(R.id.detailed_assessmentStartDate);
        assessmentEndDate = findViewById(R.id.detailed_assessmentEndDate);
        assessmentType = findViewById(R.id.detailed_assessmentType);

        String title = getIntent().getStringExtra("ASSESSMENT_TITLE");
        String date = getIntent().getStringExtra("ASSESSMENT_DATE");
        String type = getIntent().getStringExtra("ASSESSMENT_TYPE");

        assessmentTitle.setText(title);
        assessmentStartDate.setText(date);
        assessmentEndDate.setText(date);
        assessmentType.setText(type);

        assessmentStartNotification.setOnClickListener(v -> {
            String format = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            Date myDate = null;
            try {
                myDate = simpleDateFormat.parse(date);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            assert myDate != null;
            long trigger = myDate.getTime();

            Intent intent = new Intent(this, StartDateAssessmentReceiver.class);
            intent.putExtra("NEW_ASSESSMENT_START", title + " starting today!");
            pendingIntent = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

            Toast.makeText(this, "Notification Created!", Toast.LENGTH_SHORT).show();
        });

        assessmentEndNotification.setOnClickListener(v -> {
            String format = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            Date myDate = null;
            try {
                myDate = simpleDateFormat.parse(date);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            assert myDate != null;
            long trigger = myDate.getTime();

            Intent intent = new Intent(this, EndDateAssessmentReceiver.class);
            intent.putExtra("ASSESSMENT_ENDING", title + " ending today!");
            pendingIntent = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

            Toast.makeText(this, "Notification Created!", Toast.LENGTH_SHORT).show();
        });
    }
}