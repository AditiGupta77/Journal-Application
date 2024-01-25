package androidsamples.java.journalapp;
import androidx.annotation.NonNull;

public class JournalEntryData {

    public String uid, title, date, start, end, location;

    public JournalEntryData(String uid, String title, String date, String start, String end, String location){
        this.uid = uid;
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
        this.location = location;
    }
    public static JournalEntryData fromJournal(JournalEntry j){
        return new JournalEntryData((j.getUid()).toString(), j.title,j.date, j.start, j.end, j.location);
    }

    @NonNull
    @Override
    public String toString(){
        return "UID: "+this.uid+" title: "+this.title+" date: "+this.date+" start: "+this.start+" end: "+this.end+" location: "+this.location;
    }

}
