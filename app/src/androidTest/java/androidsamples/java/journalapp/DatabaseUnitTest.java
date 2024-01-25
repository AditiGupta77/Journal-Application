package androidsamples.java.journalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {

    Controller controller;
    @Rule
    public
    InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Before
    public void createDb() {
        Controller.initialize(ApplicationProvider.getApplicationContext());
        controller = Controller.getInstance();
    }

    @Test
    public void insertEntry() throws Exception{
        CountDownLatch wait = new CountDownLatch(1);
        JournalEntry entry = new JournalEntry("B-Day", "FRI, JAN 10, 2003", "07:00", "08:00");
        controller.insert(entry);

        AtomicReference<JournalEntry> list = new AtomicReference<>();
        LiveData<JournalEntry> jd = controller.getEntry(entry.getUid());
        jd.observeForever(list::set);

        wait.await(5, TimeUnit.SECONDS);
        assertEquals(entry.title, list.get().title);
        assertEquals(entry.date, list.get().date);
        assertEquals(entry.start, list.get().start);
        assertEquals(entry.end, list.get().end);
    }

    @Test
    public void updateEntry() throws Exception {
        CountDownLatch wait = new CountDownLatch(1);
        JournalEntry entry = new JournalEntry("B-Day", "FRI, JAN 10, 2003", "07:00", "08:00");
        controller.insert(entry);

        entry = new JournalEntry("Today", "THU, NOV 23, 2023", "06:00", "10:00");
        entry.setUid(entry.getUid());
        controller.update(entry);

        AtomicReference<JournalEntry> lst = new AtomicReference<>();
        LiveData<JournalEntry> jd = controller.getEntry(entry.getUid());
        jd.observeForever(lst::set);

        wait.await(5, TimeUnit.SECONDS);
        assertEquals(entry.title, lst.get().title);
        assertEquals(entry.date, lst.get().date);
        assertEquals(entry.start, lst.get().start);
        assertEquals(entry.end, lst.get().end);
    }

    @Test
    public void deleteEntry() throws Exception{

        JournalEntry entry = new JournalEntry("B-Day", "FRI, JAN 10, 2003", "07:00", "08:00");
        controller.insert(entry);
        controller.delete(entry);

        LiveData<List<JournalEntry>> allEntriesLiveData = controller.getAllEntries();
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        allEntriesLiveData.observeForever(value -> {
            data[0] = value;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        boolean present = false;
        for (JournalEntry journalEntry :  (List<JournalEntry>) data[0]) {
            if (journalEntry.getUid() == entry.getUid()) {
                present = true;
                break;
            }
        }

        assertFalse(present);

    }
}
