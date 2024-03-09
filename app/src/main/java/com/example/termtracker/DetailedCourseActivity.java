package com.example.termtracker;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DetailedCourseActivity extends AppCompatActivity implements AssessmentRecyclerViewInterface, AddAssessmentDialog.OnAssessmentEntered, EditAssessmentDialog.OnAssessmentEdited {

    private final List<Assessment> allAssessments = new ArrayList<>();
    private final List<String> assessmentInfo = new ArrayList<>();
    private TermTrackerDatabase termTrackerDatabase;
    private static RecyclerView assessmentRecyclerView;
    private AddAssessmentRecyclerAdapter assessmentAdapter;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private int position;
    public static int assessmentID;
    private final List<String> courseNote = new ArrayList<>();

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        termTrackerDatabase = TermTrackerDatabase.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_course);

        //Set ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Detailed Course View");
        }

        TextView detailedCourseTitle = findViewById(R.id.detailed_courseTitle);
        TextView detailedCourseStart = findViewById(R.id.detailed_courseStart);
        TextView detailedCourseEnd = findViewById(R.id.detailed_courseEnd);
        TextView detailedCourseStatus = findViewById(R.id.detailed_courseStatus);
        TextView detailedCourseInstructorName = findViewById(R.id.detailed_instructorName);
        TextView detailedCourseInstructorPhone = findViewById(R.id.detailed_instructorPhone);
        TextView detailedCourseInstructorEmail = findViewById(R.id.detailed_instructorEmail);
        ImageButton addAssessmentBtn = findViewById(R.id.assessmentAddBtn);
        ImageButton addNoteBtn = findViewById(R.id.noteAddBtn);
        Button saveNoteBtn = findViewById(R.id.saveNoteBtn);
        Button deleteNoteBtn = findViewById(R.id.deletedNoteBtn);
        Button shareBtn = findViewById(R.id.shareNoteBtn);
        EditText newNote = findViewById(R.id.detailed_courseNewNote);
        Button saveBtn = findViewById(R.id.courseSaveBtn);
        Button courseStartNotification = findViewById(R.id.courseStart_notification);
        Button courseEndNotification = findViewById(R.id.courseEnd_notification);
        newNote.setEnabled(false);
        saveNoteBtn.setEnabled(false);
        shareBtn.setEnabled(false);
        deleteNoteBtn.setEnabled(false);

        addNoteBtn.setOnClickListener((v -> {
            newNote.setEnabled(true);
            saveNoteBtn.setEnabled(true);
            shareBtn.setEnabled(true);
            deleteNoteBtn.setEnabled(true);
        }));

        courseNote.clear();
        courseNote.add(termTrackerDatabase.courseDao().getNote(DetailedTermActivity.selectedCourseID));

        String title = getIntent().getStringExtra("COURSE_TITLE");
        String start = getIntent().getStringExtra("COURSE_START");
        String end = getIntent().getStringExtra("COURSE_END");
        String status = getIntent().getStringExtra("COURSE_STATUS");
        String instructorName = getIntent().getStringExtra("COURSE_INSTRUCTOR_NAME");
        String instructorPhone = getIntent().getStringExtra("COURSE_INSTRUCTOR_PHONE");
        String instructorEmail = getIntent().getStringExtra("COURSE_INSTRUCTOR_EMAIL");


        detailedCourseTitle.setText(title);
        detailedCourseStart.setText(start);
        detailedCourseEnd.setText(end);
        detailedCourseStatus.setText(status);
        detailedCourseInstructorName.setText(instructorName);
        detailedCourseInstructorPhone.setText(instructorPhone);
        detailedCourseInstructorEmail.setText(instructorEmail);

        if (courseNote.size() != 0) {
            newNote.setText(courseNote.get(0));
        } else {
            newNote.setHint(R.string.newNote);
        }

        saveBtn.setOnClickListener((v -> {
            finish();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }));

        courseStartNotification.setOnClickListener(v -> {
            String format = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            Date myDate = null;
            try {
                myDate = simpleDateFormat.parse(start);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            assert myDate != null;
            long trigger = myDate.getTime();

            Intent intent = new Intent(this, StartDateCourseReceiver.class);
            intent.putExtra("NEW_COURSE_START", title + " starting today!");
            pendingIntent = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

            Toast.makeText(this, "Notification Created!", Toast.LENGTH_SHORT).show();
        });

        courseEndNotification.setOnClickListener(v -> {
            String format = "MM/dd/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            Date myDate = null;
            try {
                myDate = simpleDateFormat.parse(end);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            assert myDate != null;
            long trigger = myDate.getTime();

            Intent intent = new Intent(this, EndDateCourseReceiver.class);
            intent.putExtra("COURSE_ENDING", title + " ends today!");
            pendingIntent = PendingIntent.getBroadcast(this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

            Toast.makeText(this, "Notification Created!", Toast.LENGTH_SHORT).show();
        });

        addAssessmentBtn.setOnClickListener((v -> openAddAssessmentDialog()));

        shareBtn.setOnClickListener((v -> {
            if (courseNote.size() == 0) {
                Toast.makeText(this, "There's No Note To Share!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "New Note");
                intent.putExtra(Intent.EXTRA_TEXT, courseNote.get(0));

                Intent share = Intent.createChooser(intent, "Share Note: ");
                startActivity(share);
            }

        }));

        saveNoteBtn.setOnClickListener((v -> {
            String note = newNote.getText().toString();
            String msg = "Please Enter a Note to Save.";
            if (note.isEmpty()) {
                newNote.setError(msg);
            } else {
                courseNote.clear();
                courseNote.add(note);
                termTrackerDatabase.courseDao().updateNote(note, DetailedTermActivity.selectedCourseID);
                saveNoteBtn.setEnabled(false);
                shareBtn.setEnabled(true);
                deleteNoteBtn.setEnabled(true);
                newNote.setEnabled(false);
            }
        }));

        deleteNoteBtn.setOnClickListener((v -> {
            if (courseNote.isEmpty()) {
                Toast.makeText(this, "There is no note to delete.", Toast.LENGTH_SHORT).show();
            } else {
                termTrackerDatabase.courseDao().deleteNote(DetailedTermActivity.selectedCourseID);
                courseNote.clear();
                saveNoteBtn.setEnabled(false);
                shareBtn.setEnabled(false);
                deleteNoteBtn.setEnabled(false);
                newNote.setEnabled(false);
                newNote.setText(null);
            }
        }));

        allAssessments.addAll(termTrackerDatabase.assessmentDao().getAssessments(DetailedTermActivity.selectedCourseID));
        assessmentRecyclerView = findViewById(R.id.detailed_courseAssessmentsRecyclerView);
        assessmentAdapter = new DetailedCourseActivity.AddAssessmentRecyclerAdapter(allAssessments, this);
        assessmentRecyclerView.setAdapter(assessmentAdapter);
        assessmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(assessmentRecyclerView);
    }

    @Override
    public void onAssessmentClicked(int position) {
        Intent intent = new Intent(this, DetailedAssessmentActivity.class);
        intent.putExtra("ASSESSMENT_TITLE", allAssessments.get(position).getAssessmentTitle());
        intent.putExtra("ASSESSMENT_DATE", allAssessments.get(position).getDate());
        intent.putExtra("ASSESSMENT_TYPE", allAssessments.get(position).getType());
        startActivity(intent);
    }

    @Override
    public void onAssessmentEntered(Assessment assessment) {
        finish();
        startActivity(getIntent());
        assessmentAdapter.addAssessment(assessment);
        Toast.makeText(this, "Assessment Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAssessmentEdited(Assessment assessment) {
        assessmentAdapter.updateAssessment(assessment, position);
        Toast.makeText(this, "Assessment Edited", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        position = item.getGroupId();
        assessmentID = assessmentAdapter.getAssessmentAt(item.getGroupId()).getId();
        switch (item.getItemId()) {
            case 101:
                assessmentInfo.clear();
                assessmentInfo.add(assessmentAdapter.getAssessmentAt(item.getGroupId()).getAssessmentTitle());
                assessmentInfo.add(assessmentAdapter.getAssessmentAt(item.getGroupId()).getDate());
                assessmentInfo.add(assessmentAdapter.getAssessmentAt(item.getGroupId()).getType());
                openEditAssessmentDialog();
                return true;
            case 102:
                termTrackerDatabase.assessmentDao().deleteAssessment(assessmentAdapter.getAssessmentAt(item.getGroupId()));
                assessmentAdapter.removeAssessment(assessmentAdapter.getAssessmentAt(item.getGroupId()));
                Toast.makeText(this, "Assessment Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void openEditAssessmentDialog() {
        EditAssessmentDialog editAssessmentDialog = new EditAssessmentDialog();
        editAssessmentDialog.show(getSupportFragmentManager(), "Edit Assessment Dialog");
    }

    public void openAddAssessmentDialog() {
        AddAssessmentDialog addAssessmentDialog = new AddAssessmentDialog();
        addAssessmentDialog.show(getSupportFragmentManager(), "Add Assessment Dialog");
    }

    public List<String> getAssessmentData() {
        return assessmentInfo;
    }

    public static class AddAssessmentRecyclerAdapter extends RecyclerView.Adapter<DetailedCourseActivity.AddAssessmentRecyclerAdapter.MyViewHolder> {

        private final List<Assessment> mAssessments;
        private final AssessmentRecyclerViewInterface assessmentRecyclerViewInterface;

        private int position;

        public AddAssessmentRecyclerAdapter(List<Assessment> mAssessments, AssessmentRecyclerViewInterface assessmentRecyclerViewInterface) {
            this.mAssessments = mAssessments;
            this.assessmentRecyclerViewInterface = assessmentRecyclerViewInterface;
        }

        @NonNull
        @NotNull
        @Override
        public AddAssessmentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_assessment_row, parent, false);
            return new DetailedCourseActivity.AddAssessmentRecyclerAdapter.MyViewHolder(v, assessmentRecyclerViewInterface);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
            Assessment current = mAssessments.get(position);
            holder.titleTV.setText(current.getAssessmentTitle().trim());
            holder.dateTV.setText(current.getDate().trim());
            holder.typeTV.setText(current.getType().trim().substring(0,1));
            holder.itemView.setOnLongClickListener(v -> {
                setPosition(holder.getAdapterPosition());
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return mAssessments.size();
        }

        public Assessment getAssessmentAt(int position) {
            return mAssessments.get(position);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void addAssessment(Assessment assessment) {
            mAssessments.add(0, assessment);
            notifyItemInserted(0);
            assessmentRecyclerView.scrollToPosition(0);
        }

        public void updateAssessment(Assessment assessment, int position) {
            mAssessments.set(position, assessment);
            notifyItemChanged(position);
        }

        public void removeAssessment(Assessment assessment) {
            int index = mAssessments.indexOf(assessment);
            if (index >= 0) {
                mAssessments.remove(index);
                notifyItemRemoved(index);
            }
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView titleTV;
            TextView dateTV;
            TextView typeTV;

            public MyViewHolder(@NonNull @NotNull View itemView, AssessmentRecyclerViewInterface assessmentRecyclerViewInterface) {
                super(itemView);
                titleTV = itemView.findViewById(R.id.assessmentName);
                dateTV = itemView.findViewById(R.id.assessmentDate);
                typeTV = itemView.findViewById(R.id.assessmentType);

                itemView.setOnCreateContextMenuListener(this);

                itemView.setOnClickListener((v) -> {
                    if (assessmentRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            assessmentRecyclerViewInterface.onAssessmentClicked(pos);
                        }
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Choose an Option");
                menu.add(getAdapterPosition(), 101, 0, "Edit");
                menu.add(getAdapterPosition(), 102, 1, "Delete");
            }
        }

    }

}