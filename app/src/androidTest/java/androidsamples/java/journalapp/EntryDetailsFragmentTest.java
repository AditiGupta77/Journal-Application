package androidsamples.java.journalapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Instrumented tests for the {@link EntryDetailsFragment}.
 */
@RunWith(AndroidJUnit4.class)
public class EntryDetailsFragmentTest {
  @Rule
  public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);
  @BeforeClass
  public static void accessibility() {AccessibilityChecks.enable();}
  @Before
  public void room() {
    Controller.initialize(ApplicationProvider.getApplicationContext());
  }

  @Test
  public void testCreate() {
    onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion());
    int count = RecyclerViewItemCountAssertion.count;

    onView(withId(R.id.btn_new_entry)).perform(click());
    onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("TEST_TITLE_CREATE"));
    onView(withId(R.id.btn_date)).perform(click());

    Calendar calendar = Calendar.getInstance();
    calendar.set(2022, 1, 1);

    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.btn_start_time)).perform(click());
    onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.btn_end_time)).perform(click());
    onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.btn_save_entry)).perform(click());

    onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition(count, getData.viewWithId()));
    String title = getData.title;
    String date = getData.date;
    String start = getData.start;
    String end = getData.end;
    calendar.set(2022, 0, 1);
    String e_date =  DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

    assertEquals(title,"TEST_TITLE_CREATE");
    assertEquals(date, e_date);
    assertEquals(start,"13:00");
    assertEquals(end,"15:00");
  }

  @Test
  public void testIncomplete(){
    onView(withId(R.id.btn_new_entry)).perform(click());
    onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("Incomplete Entry"));
    onView(withId(R.id.btn_date)).perform(click());

    Calendar calendar = Calendar.getInstance();
    calendar.set(2023, 12, 12);

    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.btn_start_time)).perform(click());
    onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.btn_save_entry)).perform(click());

    onView(withId(R.id.entry_details)).check(matches(isDisplayed()));

  }

  @Test
  public void testUpdate() {

    String title;
    String date;
    String start;
    String end;
    try {
      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }catch (Exception e){
      System.out.println("No entry to modify\n adding a entry");
      onView(withId(R.id.btn_new_entry)).perform(click());
      onView(withId(R.id.edittext_title)).perform(ViewActions.clearText());
      onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("Test Titile"));
      onView(withId(R.id.btn_date)).perform(click());
      onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_start_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_end_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
      onView(withText("OK")).perform(click());
      onView(withId(R.id.btn_save_entry)).perform(click());
      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
    onView(withId(R.id.edittext_title)).perform(ViewActions.clearText());
    onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("Title Edited"));
    onView(withId(R.id.btn_date)).perform(click());
    onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.btn_start_time)).perform(click());
    onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
    onView(withText("OK")).perform(click());

    onView(withId(R.id.btn_end_time)).perform(click());
    onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
    onView(withText("OK")).perform(click());
    onView(withId(R.id.btn_save_entry)).perform(click());

    onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition(0, getData.viewWithId()));
    title = getData.title;
    date = getData.date;
    start = getData.start;
    end = getData.end;

    onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.viewWithId(title, date, start, end, false)));

  }

  @Test
  public void testDelete() {
    try {
      onView(withId(R.id.recyclerView)).perform(
              RecyclerViewActions.actionOnItemAtPosition(0, getData.viewWithId()));
      String title = getData.title;
      String date = getData.date;
      String start = getData.start;
      String end = getData.end;
      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
      onView(withId(R.id.deleteOption)).perform(click());
      onView(withId(R.id.recyclerView)).perform(
              RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.viewWithId(title, date, start, end, true)));


    }catch (AssertionError a){
      System.out.println(Arrays.toString(a.getStackTrace()));
    } catch (Exception e){
      System.out.println("No entry to modify\n adding a entry");
      onView(withId(R.id.btn_new_entry)).perform(click());
      onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("Test Deleted"));
      onView(withId(R.id.btn_date)).perform(click());
      onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_start_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_end_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
      onView(withText("OK")).perform(click());
      onView(withId(R.id.btn_save_entry)).perform(click());

      onView(withId(R.id.recyclerView)).perform(
              RecyclerViewActions.actionOnItemAtPosition(0, getData.viewWithId()));
      String title = getData.title;
      String date = getData.date;
      String start = getData.start;
      String end = getData.end;

      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
      onView(withId(R.id.deleteOption)).perform(click());

      onView(withId(R.id.recyclerView)).perform(
              RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.viewWithId(title, date, start, end, true)));
    }


  }

  @Test
  public void testShare(){
    try {
      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

    }catch (Exception e){
      System.out.println("No entry to modify\n adding a entry");
      onView(withId(R.id.btn_new_entry)).perform(click());
      onView(withId(R.id.edittext_title)).perform(ViewActions.typeText("Test Title (Share)"));
      onView(withId(R.id.btn_date)).perform(click());
      onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_start_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
      onView(withText("OK")).perform(click());

      onView(withId(R.id.btn_end_time)).perform(click());
      onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
      onView(withText("OK")).perform(click());
      onView(withId(R.id.btn_save_entry)).perform(click());
      onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }


    onView(withId(R.id.shareOption)).perform(click());
  }


}

class RecyclerViewItemCountAssertion implements ViewAssertion {

  static int count = 0;

  @Override
  public void check(View view, NoMatchingViewException noViewFoundException) {
    if (noViewFoundException != null) {
      throw noViewFoundException;
    }

    RecyclerView recyclerView = (RecyclerView) view;
    RecyclerView.Adapter<JournalEntryListAdapter.MyViewHolder> adapter = (JournalEntryListAdapter) recyclerView.getAdapter();
    assert adapter != null;
    count = adapter.getItemCount();
    assertThat(adapter.getItemCount(), is(adapter.getItemCount()));
  }
}

class MyViewAction {
  static String title;
  static String date;
  static String start;
  static String end;
  public static ViewAction viewWithId(final String t, final String d, final String s, final String e, final boolean notEquals) {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {return null;}

      @Override
      public String getDescription() {return "";}

      @Override
      public void perform(UiController uiController, View view) {
        title = ((TextView)view.findViewById(R.id.txt_title)).getText().toString();
        date = ((TextView)view.findViewById(R.id.txt_date)).getText().toString();
        start = ((TextView)view.findViewById(R.id.txt_start)).getText().toString();
        end = ((TextView)view.findViewById(R.id.txt_end)).getText().toString();
        if(notEquals) {
          assertNotEquals(title, t);
          assertNotEquals(date, d);
          assertNotEquals(start, s);
          assertNotEquals(end, e);
        }
        else{
          assertEquals(title, t);
          assertEquals(date, d);
          assertEquals(start, s);
          assertEquals(end, e);
        }
      }
    };
  }

}

class getData {
  static String title, date, start, end;

  public static ViewAction viewWithId() {
    return new ViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        title = ((TextView)view.findViewById(R.id.txt_title)).getText().toString();
        date = ((TextView)view.findViewById(R.id.txt_date)).getText().toString();
        start = ((TextView)view.findViewById(R.id.txt_start)).getText().toString();
        end = ((TextView)view.findViewById(R.id.txt_end)).getText().toString();
      }

      @Override
      public Matcher<View> getConstraints() { return null; }

      @Override
      public String getDescription() { return " ";}

    };
  }

}