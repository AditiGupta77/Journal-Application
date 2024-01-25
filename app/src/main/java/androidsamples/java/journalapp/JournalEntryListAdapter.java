package androidsamples.java.journalapp;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryListAdapter extends RecyclerView.Adapter<JournalEntryListAdapter.MyViewHolder> {
    private ArrayList<JournalEntry> entries;
    private RecyclerViewUpdateModel listener;

    public JournalEntryListAdapter(ArrayList<JournalEntry> entries) {
        this.entries = entries;
    }
    public void setListener(RecyclerViewUpdateModel listener){
        this.listener = listener;
    }

    // This method creates a new ViewHolder object for each item in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_entry, parent, false);
        return new MyViewHolder(itemView);
    }

    // Returning total number of items in dataset.
    @Override
    public int getItemCount() {
        return entries.size();
    }

    // Binding data to ViewHolder object for each item in RecyclerView.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myHolder, int pos) {
        JournalEntry entry = entries.get(pos);
        myHolder.title.setText(entry.title);
        myHolder.location.setText(entry.location);
        myHolder.date.setText(entry.date);
        myHolder.start.setText(entry.start);
        myHolder.end.setText(entry.end);
        myHolder.setJournal(entry);
        myHolder.bind(pos, listener);
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    void insertData(List<JournalEntry> entries){
        this.entries = (ArrayList<JournalEntry>) entries;
        notifyDataSetChanged();
    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final TextView start;
        private final TextView end;
        private final TextView location;
        private JournalEntry journalEntry;

        void setJournal(JournalEntry j){
            this.journalEntry = j;
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            date = itemView.findViewById(R.id.txt_date);
            start = itemView.findViewById(R.id.txt_start);
            end = itemView.findViewById(R.id.txt_end);
            location = itemView.findViewById(R.id.txt_location);
        }
        void bind(int position,  RecyclerViewUpdateModel listener){
            itemView.setOnClickListener(v -> listener.onItemClick(position, journalEntry));
        }
    }
}
