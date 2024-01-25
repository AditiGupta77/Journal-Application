package androidsamples.java.journalapp;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Controller {

    private final JournalEntryDAO mJournalEntryDao;
    private static Controller sInstance;
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    private Controller(Context con) {

        JournalEntryDatabase db =
                Room.databaseBuilder(con.getApplicationContext(),
                        JournalEntryDatabase.class,
                        "journalDB").build();

        mJournalEntryDao = db.dao();
    }

    public static void initialize(Context context) {
        if (sInstance == null) sInstance = new Controller(context);
    }

    public static Controller getInstance() {
        if (sInstance == null)
            throw new IllegalStateException("Repo must be initialized");
        return sInstance;
    }

    public void insert(JournalEntry journalEntry){
        mExecutor.execute(() ->  mJournalEntryDao.insertJournal(journalEntry));
    }
    public void delete(JournalEntry journalEntry){
        mExecutor.execute(() ->  mJournalEntryDao.deleteJournal(journalEntry));
    }
    public void update(JournalEntry journalEntry){
        mExecutor.execute(() ->  mJournalEntryDao.updateJournal(journalEntry));
    }
    public LiveData<List<JournalEntry>> getAllEntries(){
        return mJournalEntryDao.getAll();
    }
    public LiveData<JournalEntry> getEntry(UUID uid){
        return mJournalEntryDao.getEntry(uid);
    }
}

