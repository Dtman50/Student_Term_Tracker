package com.example.termtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddCourseDialog extends AppCompatDialogFragment {

    private EditText startDate;
    private EditText endDate;
    private String status;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<CharSequence> statusAdapter;

    private OnCourseEntered listener;

    public interface OnCourseEntered {
        void onCourseEntered(Course course);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_courses_dialog, null);
        autoCompleteTextView = view.findViewById(R.id.auto_complete);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.CourseStatus, R.layout.list_item);
        statusAdapter.setDropDownViewResource(R.layout.list_item);
        autoCompleteTextView.setAdapter(statusAdapter);
        autoCompleteTextView.setOnItemClickListener((parent, view12, position, id) -> {
            status = parent.getItemAtPosition(position).toString();
        });

        startDate = view.findViewById(R.id.addCourseStart);
        endDate = view.findViewById(R.id.addCourseEnd);
        ImageButton startBtn = view.findViewById(R.id.courseStartDatePickBtn);
        ImageButton endBtn = view.findViewById(R.id.courseEndDatePickBtn);

        startBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddCourseDialog.this.getContext(),
                    (view1, year1, month1, dayOfMonth) -> startDate.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        endBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddCourseDialog.this.getContext(),
                    (view2, year12, month12, dayOfMonth) -> endDate.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);
            datePickerDialog.show();
        });

        builder.setView(view)
                .setTitle("Add a Course")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    TermTrackerDatabase termTrackerDatabase;
                    termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());

                    EditText courseTitle = view.findViewById(R.id.addCourseTitle);
                    EditText courseStart = view.findViewById(R.id.addCourseStart);
                    EditText courseEnd = view.findViewById(R.id.addCourseEnd);
                    EditText courseInstructorName = view.findViewById(R.id.instructorName);
                    EditText courseInstructorPhone = view.findViewById(R.id.instructorPhone);
                    EditText courseInstructorEmail = view.findViewById(R.id.instructorEmail);

                    String title = courseTitle.getText().toString();
                    String start = courseStart.getText().toString();
                    String end = courseEnd.getText().toString();
                    String instructorName = courseInstructorName.getText().toString();
                    String instructorPhone = courseInstructorPhone.getText().toString();
                    String instructorEmail = courseInstructorEmail.getText().toString();
                    int termID = TermActivity.selectedTermID;

                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(start)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(end)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorName)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorPhone)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorEmail)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    } else {
                        Course course = new Course(title, start, end, status, instructorName, instructorPhone, instructorEmail, termID);
                        listener.onCourseEntered(course);
                        termTrackerDatabase.courseDao().insertCourse(course);
                    }

                });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (OnCourseEntered) context;
    }
}
