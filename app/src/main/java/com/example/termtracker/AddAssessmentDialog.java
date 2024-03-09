package com.example.termtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddAssessmentDialog extends AppCompatDialogFragment {
    private EditText date;

    private AddAssessmentDialog.OnAssessmentEntered listener;

    public interface OnAssessmentEntered {
        void onAssessmentEntered(Assessment assessment);
    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_assessment_dialog, null);


        builder.setView(view)
                .setTitle("Add an Assessment")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    TermTrackerDatabase termTrackerDatabase;
                    termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());
                    EditText assessmentTitle = view.findViewById(R.id.addAssessmentTitle);
                    EditText assessmentDate = view.findViewById(R.id.addAssessmentDate);
                    RadioGroup radioGroup = view.findViewById(R.id.addAssessmentTypeRadioGroup);
                    RadioButton radioButton;
                    int checkedID = radioGroup.getCheckedRadioButtonId();
                    radioButton = view.findViewById(checkedID);

                    String title = assessmentTitle.getText().toString();
                    String date = assessmentDate.getText().toString();
                    try {
                        String type = radioButton.getText().toString();


                        int courseID = DetailedTermActivity.selectedCourseID;

                        if (TextUtils.isEmpty(title)) {
                            Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(date)) {
                            Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                        } else if (TextUtils.isEmpty(type)) {
                            Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                        } else {
                            Assessment assessment = new Assessment(title, date, type, courseID);
                            listener.onAssessmentEntered(assessment);
                            termTrackerDatabase.assessmentDao().insertAssessment(assessment);
                        }
                    } catch (NullPointerException ne) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Select A Type", Toast.LENGTH_SHORT).show();
                    }
                });

        date = view.findViewById(R.id.addAssessmentDate);
        ImageButton dateBtn = view.findViewById(R.id.assessmentDatePickBtn);

        dateBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddAssessmentDialog.this.getContext(),
                    (view1, year1, month1, dayOfMonth) -> date.setText((month1 + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (OnAssessmentEntered) context;
    }

}