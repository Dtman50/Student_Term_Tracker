package com.example.termtracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.lifecycle.ViewModelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class AddTermDialog extends AppCompatDialogFragment {
    private EditText startDate;
    private EditText endDate;

    public interface OnTermEntered {
        void onTermEntered(Term term);
    }

    private OnTermEntered listener;

    private Term term;

//    private final TermTrackerViewModel termTrackerViewModel = new ViewModelProvider(this).get(TermTrackerViewModel.class);


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_terms_dialog, null);

        builder.setView(view)
                .setTitle("Add a Term")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Ok", (dialog, which) -> {
                    EditText titleTV = view.findViewById(R.id.addTermTitle);
                    EditText startTV = view.findViewById(R.id.addTermStart);
                    EditText endTV = view.findViewById(R.id.addTermEnd);

                    String title = titleTV.getText().toString();
                    String start = startTV.getText().toString();
                    String end = endTV.getText().toString();

                    TermTrackerDatabase termTrackerDatabase;

                    if (TextUtils.isEmpty(title)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(start)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    }
                    else if (TextUtils.isEmpty(end)) {
                        Toast.makeText(getContext(), "ERROR ADD FAILED: Please Enter All Fields", Toast.LENGTH_LONG).show();
                    } else {
                        termTrackerDatabase = TermTrackerDatabase.getInstance(getContext());
                        term = new Term(title, start, end);
                        listener.onTermEntered(term);
                        termTrackerDatabase.termDao().insertTerm(term);
                    }
                });


        startDate = view.findViewById(R.id.addTermStart);
        endDate = view.findViewById(R.id.addTermEnd);
        ImageButton startBtn = view.findViewById(R.id.startDatePickBtn);
        ImageButton endBtn = view.findViewById(R.id.endDatePickBtn);

        startBtn.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTermDialog.this.getContext(),
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
                    AddTermDialog.this.getContext(),
                    (view2, year12, month12, dayOfMonth) -> endDate.setText((month12 + 1) + "/" + dayOfMonth + "/" + year12), year, month, day);
            datePickerDialog.show();
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        listener = (OnTermEntered) context;
    }

}
