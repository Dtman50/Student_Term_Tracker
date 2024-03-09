package com.example.termtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailedTermActivity extends AppCompatActivity implements CourseRecyclerViewInterface, AddCourseDialog.OnCourseEntered, EditCourseDialog.OnCourseEdited {

    private final List<Course> allCourses = new ArrayList<>();
    private final List<String> courseInfo = new ArrayList<>();
    private TermTrackerDatabase termTrackerDatabase;
    private static RecyclerView recyclerView;
    private AddCourseRecyclerAdapter adapter;
    private static int position;
    public static int courseID;
    public static int selectedCourseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_term);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Detailed Term View");

        Button saveBtn = findViewById(R.id.termSaveBtn);
        TextView detailedTitle = findViewById(R.id.detailed_termTitleTV);
        TextView detailedStart = findViewById(R.id.detailed_termStartTV);
        TextView detailedEnd = findViewById(R.id.detailed_termEndTV);
        ImageButton addCourseBtn = findViewById(R.id.addCourseBtn);

        String title = getIntent().getStringExtra("TITLE");
        String start = getIntent().getStringExtra("START");
        String end = getIntent().getStringExtra("END");

        detailedTitle.setText(title);
        detailedStart.setText(start);
        detailedEnd.setText(end);

        saveBtn.setOnClickListener((v -> {
            finish();
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        }));

        addCourseBtn.setOnClickListener((v -> {
            openAddCourseDialog();
        }));

        termTrackerDatabase = TermTrackerDatabase.getInstance(getApplicationContext());
        allCourses.addAll(termTrackerDatabase.courseDao().getCourses(TermActivity.selectedTermID));
        recyclerView = findViewById(R.id.termCourseRecyclerView);
        adapter = new AddCourseRecyclerAdapter(allCourses, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(recyclerView);

    }

    @Override
    public void onCourseClicked(int position) {
        selectedCourseID = adapter.getCourseAt(position).getId();
        System.out.println(selectedCourseID);
        Intent intent = new Intent(this, DetailedCourseActivity.class);
        intent.putExtra("COURSE_TITLE", allCourses.get(position).getCourseTitle());
        intent.putExtra("COURSE_START", allCourses.get(position).getStart());
        intent.putExtra("COURSE_END", allCourses.get(position).getEnd());
        intent.putExtra("COURSE_STATUS", allCourses.get(position).getStatus());
        intent.putExtra("COURSE_INSTRUCTOR_NAME", allCourses.get(position).getInstructorName());
        intent.putExtra("COURSE_INSTRUCTOR_PHONE", allCourses.get(position).getInstructorPhone());
        intent.putExtra("COURSE_INSTRUCTOR_EMAIL", allCourses.get(position).getInstructorEmail());
        startActivity(intent);
    }

    @Override
    public void onCourseEntered(Course course) {
        finish();
        startActivity(getIntent());
        adapter.addCourse(course);
        Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCourseEdited(Course course) {
        adapter.updateCourse(course, position);
        Snackbar.make(findViewById(R.id.mainDetailedTerm), "Course Edited", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        position = item.getGroupId();
        courseID = adapter.getCourseAt(item.getGroupId()).getId();
        switch (item.getItemId()) {
            case 101:
                courseInfo.clear();
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getCourseTitle());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getStart());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getEnd());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getStatus());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getInstructorName());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getInstructorPhone());
                courseInfo.add(adapter.getCourseAt(item.getGroupId()).getInstructorEmail());
                openEditTermDialog();
                return true;
            case 102:
                try {
                    termTrackerDatabase.courseDao().deleteCourse(adapter.getCourseAt(item.getGroupId()));
                    adapter.removeCourse(adapter.getCourseAt(item.getGroupId()));
                    Snackbar.make(findViewById(R.id.mainDetailedTerm), "Course Deleted", Snackbar.LENGTH_LONG).show();
                } catch (SQLiteConstraintException se) {
                    Snackbar.make(findViewById(R.id.mainDetailedTerm), "Can't Delete a Course if it Has Assessments. Delete Assessments First.", Snackbar.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    public void openAddCourseDialog() {
        AddCourseDialog addCourseDialog = new AddCourseDialog();
        addCourseDialog.show(getSupportFragmentManager(), "Add Course Dialog");
    }

    public void openEditTermDialog() {
        EditCourseDialog editCourseDialog = new EditCourseDialog();
        editCourseDialog.show(getSupportFragmentManager(), "Edit Course Dialog");
    }

    public List<String> getMyData(){
        return courseInfo;
    }

    public static class AddCourseRecyclerAdapter extends RecyclerView.Adapter<DetailedTermActivity.AddCourseRecyclerAdapter.MyViewHolder> {
        private final List<Course> mCourses;
        private final CourseRecyclerViewInterface courseRecyclerViewInterface;
        private int position;

        public AddCourseRecyclerAdapter(List<Course> mCourses, CourseRecyclerViewInterface courseRecyclerViewInterface) {
            this.mCourses = mCourses;
            this.courseRecyclerViewInterface = courseRecyclerViewInterface;
        }

        @NonNull
        @NotNull
        @Override
        public DetailedTermActivity.AddCourseRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_course_row, parent, false);
            return new DetailedTermActivity.AddCourseRecyclerAdapter.MyViewHolder(v, courseRecyclerViewInterface);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
            Course current = mCourses.get(position);
            holder.titleTV.setText(current.getCourseTitle());
            holder.startTV.setText(current.getStart());
            holder.endTV.setText(current.getEnd());
            holder.itemView.setOnLongClickListener(v -> {
                setPosition(holder.getAdapterPosition());
                return false;
            });

        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

        public Course getCourseAt(int position) {
            return mCourses.get(position);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void addCourse(Course course) {
            mCourses.add(0, course);
            notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }

        public void updateCourse(Course course, int position) {
            mCourses.set(position, course);
            notifyItemChanged(position);
        }

        public void removeCourse(Course course) {
            int index = mCourses.indexOf(course);
            if (index >= 0) {
                mCourses.remove(index);
                notifyItemRemoved(index);
            }
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            TextView titleTV;
            TextView startTV;
            TextView endTV;

            public MyViewHolder(@NonNull @NotNull View itemView, CourseRecyclerViewInterface courseRecyclerViewInterface) {
                super(itemView);
                titleTV = itemView.findViewById(R.id.courseName);
                startTV = itemView.findViewById(R.id.courseStart);
                endTV = itemView.findViewById(R.id.courseEnd);

                itemView.setOnCreateContextMenuListener(this);

                itemView.setOnClickListener((v) -> {
                    if (courseRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            courseRecyclerViewInterface.onCourseClicked(pos);
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