package androidsamples.java.journalapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class JournalEntry {

    public static JournalEntry fromJournalData(JournalEntryData journalData){
        JournalEntry journalEntry = new JournalEntry(journalData.title,journalData.date,journalData.start,journalData.end,journalData.location);
        journalEntry.setUid(UUID.fromString(journalData.uid));
        return journalEntry;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private UUID uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "duration")
    public String date;
    @ColumnInfo(name = "location")
    public String location;

    public JournalEntry(@NonNull String title, String date, String start, String end, String location) {
        uid = UUID.randomUUID();
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    @ColumnInfo(name = "start")
    public String start;

    @ColumnInfo(name = "end")
    public String end;

    @NonNull
    public UUID getUid() {
        return uid;
    }

    public void setUid(@NonNull UUID uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString(){
        return "UID: "+this.uid+" title: "+this.title+" date: "+this.date+" start: "+this.start+" end: "+this.end + " location: " + this.location;
    }
}