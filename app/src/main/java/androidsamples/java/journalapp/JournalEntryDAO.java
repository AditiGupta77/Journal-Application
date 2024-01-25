package androidsamples.java.journalapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface JournalEntryDAO {
    @Query("SELECT * FROM JournalEntry")
    LiveData<List<JournalEntry>> getAll();

    @Query("SELECT * from JournalEntry WHERE id=(:id)")
    LiveData<JournalEntry> getEntry(UUID id);

    @Insert
    void insertJournal(JournalEntry journals);

    @Delete
    void deleteJournal(JournalEntry journalEntry);

    @Update
    void updateJournal(JournalEntry journalEntry);
}
