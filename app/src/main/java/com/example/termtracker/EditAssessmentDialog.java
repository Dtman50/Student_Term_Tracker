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
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class EditAssessmentDialog extends AppCompatDialogFragment {

    private EditText date;
    private EditText assessmentTitle;
    private EditText assessmentDate;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditAssessmentDialog.OnAssessmentEdited listener;

    public interface OnAssessmentEdited {
        void onAssessmentEdited(Assessment assessment);
    }

    @SuppressLint("CutPasteId")
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DetailedCourseActivity activity = (DetailedCourseActivity) getActivity();
        assert activity != null;
        List<String> assessmentData = activity.getAssessmentData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_assessment_dialog, null);

        assessmentTitle = view.findViewById(R.id.editAssessmentTitle);
        assessmentDate = view.findViewById(R.id.editAssessmentDate);

        assessmentTitle.setText(assessmentData.get(0));
        assessmentDate.setText(assessmentData.get(1));

        builder.setView(view)
                .setTitle("Edit Assessment")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    TermTrackerDatabase termTrackerDatabase;
                    termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());
                    radioGroup = view.findViewById(R.id.editAssessmentTypeRadioGroup);
                    int checkedID = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(checkedID);

                    String title = assessmentTitle.getText().toString();
                    String date = assessmentDate.getText().toString();
                    String type = radioButton.getText().toString();
                    int courseID = DetailedTermActivity.selectedCourseID;

                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(date)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(type)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    } else {
                        Assessment assessment = new Assessment(title, date, type, courseID);
                        listener.onAssessmentEdited(assessment);
                        assessment.setId(DetailedCourseActivity.assessmentID);
                        termTrackerDatabase.assessmentDao().updateAssessment(assessment);
                    }
                });

        date = view.findViewById(R.id.editAssessmentDate);
        ImageButton dateBtn = view.findViewById(R.id.editAssessmentDatePickBtn);

        dateBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditAssessmentDialog.this.getContext(),
                    (view1, year1, month1, dayOfMonth) ->date.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (OnAssessmentEdited) context;
    }
}