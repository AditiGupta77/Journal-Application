package androidsamples.java.journalapp;

import androidx.lifecycle.ViewModel;

public class EntryDetailsViewModel extends ViewModel {

    private final Controller controller;
    String title, date, startTime, endTime, location;
    public EntryDetailsViewModel(){
        controller = Controller.getInstance();
        title = "";
        date = "Date";
        startTime = "Start Time";
        endTime = "End Time";
        location= "";
    }

    void delete(JournalEntry journalEntry){
        controller.delete(journalEntry);
    }
    void update(JournalEntry journalEntry){
        controller.update(journalEntry);
    }
    void insert(JournalEntry journalEntry){
        controller.insert(journalEntry);
    }
}
