package com.example.termtracker;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TermActivity extends AppCompatActivity implements AddTermDialog.OnTermEntered, TermRecyclerViewInterface, EditTermDialog.OnTermEdited{

//    public static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;
    private static RecyclerView recyclerView;
    private AddTermRecyclerAdapter adapter;
    private final List<Term> allTerms = new ArrayList<>();
    private TermTrackerDatabase termTrackerDatabase;
    public static int mPosition;
    public static int termId;
    public static int selectedTermID;
//    public static int testID;
    private final List<String> termInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        termTrackerDatabase = TermTrackerDatabase.getInstance(getApplicationContext());
        FloatingActionButton addBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        //Set ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Terms");

        addBtn = findViewById(R.id.addTermBtn);
        addBtn.setOnClickListener(v -> openAddTermDialog());

        allTerms.addAll(termTrackerDatabase.termDao().getTerms());

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new AddTermRecyclerAdapter(allTerms, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(recyclerView);

    }

    public void openAddTermDialog() {
        AddTermDialog addTermDialog = new AddTermDialog();
        addTermDialog.show(getSupportFragmentManager(), "Add Term Dialog");
    }

    public void openEditTermDialog() {
        EditTermDialog editTermDialog = new EditTermDialog();
        editTermDialog.show(getSupportFragmentManager(), "Edit Term Dialog");
    }

    @RequiresApi(api = 34)
    @Override
    public void onTermEntered(Term term) {
        finish();
        startActivity(getIntent());
        adapter.addTerm(term);
        Toast.makeText(this, "Term Added", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTermEdited(Term term) {
        adapter.updateTerm(term, mPosition);
        Snackbar.make(findViewById(R.id.mainTerm), "Term Edited", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTermClicked(int position) {
        selectedTermID = adapter.getTermAt(position).getId();
        System.out.println(selectedTermID);
        Intent intent = new Intent(this, DetailedTermActivity.class);
        intent.putExtra("TITLE", allTerms.get(position).getTermTitle());
        intent.putExtra("START", allTerms.get(position).getStart());
        intent.putExtra("END", allTerms.get(position).getEnd());
        startActivity(intent);

    }

    public List<String> getMyData(){
        return termInfo;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        mPosition = item.getGroupId();
        termId = adapter.getTermAt(item.getGroupId()).getId();
        switch (item.getItemId()) {
            case 101:
                termInfo.clear();
                termInfo.add(adapter.getTermAt(item.getGroupId()).getTermTitle());
                termInfo.add(adapter.getTermAt(item.getGroupId()).getStart());
                termInfo.add(adapter.getTermAt(item.getGroupId()).getEnd());
                openEditTermDialog();
                return true;
            case 102:
                try {
                    termTrackerDatabase.termDao().deleteTerm(adapter.getTermAt(item.getGroupId()));
                    adapter.removeTerm(adapter.getTermAt(item.getGroupId()));
                    Snackbar.make(findViewById(R.id.mainTerm), "Term Deleted", Snackbar.LENGTH_LONG).show();
                } catch (SQLiteConstraintException se) {
                    Snackbar.make(findViewById(R.id.mainTerm), "Can't Delete a Term if it Has Courses. Delete Courses First.", Snackbar.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public static class AddTermRecyclerAdapter extends RecyclerView.Adapter<AddTermRecyclerAdapter.MyViewHolder> {
        private final List<Term> mTerms;
        private final TermRecyclerViewInterface termRecyclerViewInterface;
        private int position1;


        public AddTermRecyclerAdapter(List<Term> mTerms, TermRecyclerViewInterface termRecyclerViewInterface) {
            this.mTerms = mTerms;
            this.termRecyclerViewInterface = termRecyclerViewInterface;
        }

        @NonNull
        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_term_row, parent, false);
            return new MyViewHolder(v, termRecyclerViewInterface);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
            Term current = mTerms.get(position);
            holder.titleTV.setText(current.getTermTitle());
            holder.startTV.setText(current.getStart());
            holder.endTV.setText(current.getEnd());
            holder.itemView.setOnLongClickListener(v -> {
                setPosition(holder.getAdapterPosition());
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return mTerms.size();
        }
        public Term getTermAt(int position) {
            return mTerms.get(position);
        }
        public int getPosition() {
            return position1;
        }

        public void setPosition(int position) {
            this.position1 = position;
        }

        public void addTerm(Term term) {
            mTerms.add(0, term);
            notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }

        public void updateTerm(Term term, int position) {
            mTerms.set(position, term);
            notifyItemChanged(position);
        }

        public void removeTerm(Term term) {
            int index = mTerms.indexOf(term);
            if (index >= 0) {
                mTerms.remove(index);
                notifyItemRemoved(index);
            }
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
            TextView titleTV;
            TextView startTV;
            TextView endTV;
            CardView cardView;

            public MyViewHolder(@NonNull @NotNull View itemView, TermRecyclerViewInterface termRecyclerViewInterface) {
                super(itemView);
                titleTV = itemView.findViewById(R.id.term);
                startTV = itemView.findViewById(R.id.startDate);
                endTV = itemView.findViewById(R.id.endDate);
                cardView = itemView.findViewById(R.id.cardView);

                itemView.setOnCreateContextMenuListener(this);

                itemView.setOnClickListener((v) -> {
                    if (termRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            termRecyclerViewInterface.onTermClicked(pos);
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