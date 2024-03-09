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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class EditTermDialog extends AppCompatDialogFragment {
    private EditText startDate;
    private EditText endDate;
    private EditText titleTV;
    private EditText startTV;
    private EditText endTV;

    public interface OnTermEdited {
        void onTermEdited(Term term);
    }

    private OnTermEdited listener;


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_term_dialog, null);

        TermActivity activity = (TermActivity) getActivity();
        assert activity != null;
        List<String> termData = activity.getMyData();

        titleTV = view.findViewById(R.id.editTermTitle);
        startTV = view.findViewById(R.id.editTermStart);
        endTV = view.findViewById(R.id.editTermEnd);

        titleTV.setText(termData.get(0));
        startTV.setText(termData.get(1));
        endTV.setText(termData.get(2));

        builder.setView(view)
                .setTitle("Edit Term")
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    TermTrackerDatabase termTrackerDatabase;
                    termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());

                    String title = titleTV.getText().toString();
                    String start = startTV.getText().toString();
                    String end = endTV.getText().toString();

                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(start)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(end)) {
                        Toast.makeText(getContext(), "ERROR EDIT FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    } else {
                        Term term = new Term(title, start, end);
                        listener.onTermEdited(term);
                        term.setId(TermActivity.termId);
                        termTrackerDatabase.termDao().updateTerm(term);
                    }
                });

        startDate = view.findViewById(R.id.editTermStart);
        endDate = view.findViewById(R.id.editTermEnd);
        ImageButton startBtn = view.findViewById(R.id.startDatePickBtn);
        ImageButton endBtn = view.findViewById(R.id.endDatePickBtn);

        startBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    EditTermDialog.this.getContext(),
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
                    EditTermDialog.this.getContext(),
                    (view2, year12, month12, dayOfMonth) -> endDate.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);
            datePickerDialog.show();
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (OnTermEdited) context;
    }
}