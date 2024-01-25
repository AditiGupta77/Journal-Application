package androidsamples.java.journalapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EntryListViewModel extends ViewModel {

    private final Controller controller;

    public EntryListViewModel(){
        controller = Controller.getInstance();
    }

    LiveData<List<JournalEntry>> getAllEntries(){
        return controller.getAllEntries();
    }
}
