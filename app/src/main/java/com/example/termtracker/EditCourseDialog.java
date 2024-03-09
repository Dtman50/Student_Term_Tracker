package com.example.termtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class EditCourseDialog extends AppCompatDialogFragment {

    private EditText startDate;
    private EditText endDate;
    private EditText courseTitle;
    private EditText courseStart;
    private EditText courseEnd;
    private EditText courseInstructorName;
    private EditText courseInstructorPhone;
    private EditText courseInstructorEmail;
    private String status;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<CharSequence> statusAdapter;

    private EditCourseDialog.OnCourseEdited listener;

    public interface OnCourseEdited {
        void onCourseEdited(Course course);
    }

    @SuppressLint("CutPasteId")
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DetailedTermActivity activity = (DetailedTermActivity) getActivity();
        assert activity != null;
        List<String> courseData = activity.getMyData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_course_dialog, null);
        TextInputLayout statusDropDown = view.findViewById(R.id.statusDropDown);
        autoCompleteTextView = view.findViewById(R.id.auto_complete);
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.CourseStatus, R.layout.list_item);
        statusAdapter.setDropDownViewResource(R.layout.list_item);
        autoCompleteTextView.setAdapter(statusAdapter);
        autoCompleteTextView.setOnItemClickListener((parent, view12, position, id) -> status = parent.getItemAtPosition(position).toString());

        courseTitle = view.findViewById(R.id.editCourseTitle);
        courseStart = view.findViewById(R.id.editCourseStart);
        courseEnd = view.findViewById(R.id.editCourseEnd);
        courseInstructorName = view.findViewById(R.id.editInstructorName);
        courseInstructorPhone = view.findViewById(R.id.editInstructorPhone);
        courseInstructorEmail = view.findViewById(R.id.editInstructorEmail);

        courseTitle.setText(courseData.get(0));
        courseStart.setText(courseData.get(1));
        courseEnd.setText(courseData.get(2));
        statusDropDown.setHint(courseData.get(3));
        courseInstructorName.setText(courseData.get(4));
        courseInstructorPhone.setText(courseData.get(5));
        courseInstructorEmail.setText(courseData.get(6));

        builder.setView(view)
                .setTitle("Edit Course")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    TermTrackerDatabase termTrackerDatabase;
                    termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());

                    String title = courseTitle.getText().toString();
                    String start = courseStart.getText().toString();
                    String end = courseEnd.getText().toString();
                    String instructorName = courseInstructorName.getText().toString();
                    String instructorPhone = courseInstructorPhone.getText().toString();
                    String instructorEmail = courseInstructorEmail.getText().toString();
                    int termID = TermActivity.selectedTermID;

                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(start)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(end)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorName)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorPhone)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(instructorEmail)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    } else {
                        Course course = new Course(title, start, end, status, instructorName, instructorPhone, instructorEmail, termID);
                        listener.onCourseEdited(course);
                        course.setId(DetailedTermActivity.courseID);
                        termTrackerDatabase.courseDao().updateCourse(course);
                    }
                });

        startDate = view.findViewById(R.id.editCourseStart);
        endDate = view.findViewById(R.id.editCourseEnd);
        ImageButton startBtn = view.findViewById(R.id.editCourseStartDatePickBtn);
        ImageButton endBtn = view.findViewById(R.id.editCourseEndDatePickBtn);

        startBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditCourseDialog.this.getContext(),
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
                    EditCourseDialog.this.getContext(),
                    (view2, year12, month12, dayOfMonth) -> endDate.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);
            datePickerDialog.show();
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (EditCourseDialog.OnCourseEdited) context;
    }

}