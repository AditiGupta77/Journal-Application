package androidsamples.java.journalapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class EntryDetailsFragment extends Fragment{
  Boolean dateFlag = false;
  Boolean timeFlag = false;
  Boolean startFlag = false;
  Boolean endFlag = false;
  String stringStart = "";
  String stringEnd = "";
  Button dateBtn;
  Button startBtn;
  Button endBtn;
  EditText titleText;
  EditText locationText;
  Button saveBtn;
  NavBackStackEntry viewModelEntryBackStackNav;
  NavBackStackEntry entryBackStackNav;
  NavController controllerNav;
  MainViewModel mainViewModel;
  EntryDetailsViewModel detailsViewModelEntry;
  JournalEntryData dataEntry;
  int existFlag;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @SuppressLint("LongLogTag")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View inflatedView = inflater.inflate(R.layout.fragment_entry_details, container, false);
    dateBtn = inflatedView.findViewById(R.id.btn_date);
    startBtn= inflatedView.findViewById(R.id.btn_start_time);
    endBtn = inflatedView.findViewById(R.id.btn_end_time);
    saveBtn = inflatedView.findViewById(R.id.btn_save_entry);

    titleText = inflatedView.findViewById(R.id.edittext_title);
    locationText = inflatedView.findViewById(R.id.location);
    Toolbar toolbarEntry = inflatedView.findViewById(R.id.entry_toolbar);
    detailsViewModelEntry = new ViewModelProvider(this).get(EntryDetailsViewModel.class);

    MenuProvider providerMenu = new MenuProvider() {
      @Override
      public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.entry_menu, menu);
      }

      @Override
      public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.deleteOption){
          deleteOption();
        }else if(id == R.id.shareOption){
          shareOption();
        }
        return false;
      }
    };
    toolbarEntry.addMenuProvider(providerMenu);

    endBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EntryDetailsFragmentDirections.TimePickerDialog actionTimePicker =
                EntryDetailsFragmentDirections.timePickerDialog(getString(R.string.TYPE_END));
        controllerNav.navigate(actionTimePicker);
      }
    });

    startBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        EntryDetailsFragmentDirections.TimePickerDialog actionTimePicker =
                EntryDetailsFragmentDirections.timePickerDialog(getString(R.string.TYPE_START));
        controllerNav.navigate(actionTimePicker);
      }
    });

    dateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        NavDirections actionDatePicker = EntryDetailsFragmentDirections.datePickerDialog();
        controllerNav.navigate(actionDatePicker);
      }
    });

    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(dateFlag && timeFlag && titleText.getText().toString().length() != 0 && locationText.getText().toString().length() != 0){
          startFlag = false;
          endFlag = false;
          timeFlag = false;
          dateFlag = false;
          detailsViewModelEntry.title = titleText.getText().toString();
          detailsViewModelEntry.location = locationText.getText().toString();
          Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
          updateUI();
          saveJournal();
        }
        else{
          String toastOutput = "";
          if(titleText.getText().toString().equals("")){
            toastOutput = "Enter title";
          }
          else if(locationText.getText().toString().equals("")){
            toastOutput = "Enter Location";
          }
          else if(!startFlag){
            toastOutput = "Enter start time";
          }
          else if(!endFlag){
            toastOutput = "Enter end time";
          }
          else if(!dateFlag){
            toastOutput = "Enter correct date";
          }
          else if(!timeFlag){
            toastOutput = "Start should be before End";
          }
          Toast.makeText(getContext(), toastOutput, Toast.LENGTH_SHORT).show();
        }
      }
    });
    return inflatedView;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    controllerNav = Navigation.findNavController(view);
    viewModelEntryBackStackNav = controllerNav.getBackStackEntry(R.id.nav_graph);

    mainViewModel = new ViewModelProvider(viewModelEntryBackStackNav).get(MainViewModel.class);

    entryBackStackNav = controllerNav.getBackStackEntry(R.id.entryDetailsFragment);

    EntryDetailsFragmentArgs args = EntryDetailsFragmentArgs.fromBundle(getArguments());

    existFlag = args.getOpCreateUpdateDelete();
    if (existFlag != 1 && args.getJEntry()) {
      dataEntry = mainViewModel.journalData;
      if(dataEntry == null){
        controllerNav.popBackStack();
      }
      detailsViewModelEntry.date = dataEntry.date;
      detailsViewModelEntry.endTime = dataEntry.end;
      detailsViewModelEntry.startTime = dataEntry.start;
      detailsViewModelEntry.title = dataEntry.title;
      detailsViewModelEntry.location = dataEntry.location;
      startFlag = true;
      endFlag = true;
      timeFlag = true;
      dateFlag = true;
      updateUI();
    }
    entryBackStackNav.getLifecycle().addObserver(timeObserver);
    entryBackStackNav.getLifecycle().addObserver(dateObserver);

    // removeObserver() used to destroy observer when the view lifecycle is destroyed

    getViewLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
      if (event.equals(Lifecycle.Event.ON_DESTROY)) {
        entryBackStackNav.getLifecycle().removeObserver(dateObserver);
        entryBackStackNav.getLifecycle().removeObserver(timeObserver);
      }
    });
  }

  void updateUI(){
    String titleTemp = titleText.getText().toString();
    String locationTemp = locationText.getText().toString();
    if(titleTemp.length() != 0) {
      detailsViewModelEntry.title = titleTemp;
    }
    if(locationTemp.length()!=0)
    {
      detailsViewModelEntry.location = locationTemp;
    }
    dateBtn.setText(detailsViewModelEntry.date);
    startBtn.setText(detailsViewModelEntry.startTime);
    endBtn.setText(detailsViewModelEntry.endTime);
    titleText.setText(detailsViewModelEntry.title);
    locationText.setText(detailsViewModelEntry.location);
  }

  public void shareOption(){
    if(dateFlag && timeFlag && !(titleText.getText().toString().equals(""))) {
      String shareString = "Look what I have been up to :"
              + titleText.getText().toString()
              + " on "
              + dateBtn.getText().toString()
              + " from "
              + startBtn.getText().toString()
              + " to "
              + endBtn.getText().toString()
              +  " !" + "At "+ locationText.getText().toString();

      Intent intentSharing = new Intent(Intent.ACTION_SEND);
      intentSharing.setType("text/plain");
      String subjectShare = "Share";
      intentSharing.putExtra(Intent.EXTRA_TEXT, shareString);
      intentSharing.putExtra(Intent.EXTRA_SUBJECT, subjectShare);
      startActivity(Intent.createChooser(intentSharing, "Share using"));

      Toast.makeText(getContext(), R.string.sharing, Toast.LENGTH_SHORT).show();
    }
    else{
      Toast.makeText(getContext(), "Incomplete Entries", Toast.LENGTH_SHORT).show();
    }
  }

  public void deleteOption(){

    AlertDialog.Builder deleteBtn = new AlertDialog.Builder(requireContext());

    deleteBtn.setMessage("Are you sure you want to delete the Entry?");   //  ALERT! MESSAGE
    deleteBtn.setTitle("Delete Entry?");
    deleteBtn.setCancelable(false);

    deleteBtn.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (existFlag != 1) {
          Toast.makeText(getContext(), "Deleting..", Toast.LENGTH_SHORT).show();
          JournalEntry journalEntry = new JournalEntry(detailsViewModelEntry.title, detailsViewModelEntry.date, detailsViewModelEntry.startTime, detailsViewModelEntry.endTime,detailsViewModelEntry.location);
          journalEntry.setUid(UUID.fromString(dataEntry.uid));
          Log.d("DELETE", journalEntry.toString());
          detailsViewModelEntry.delete(JournalEntry.fromJournalData(dataEntry));
        }
        controllerNav.popBackStack();
      }

    });

    deleteBtn.setNegativeButton("No", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });

    AlertDialog alertDelete = deleteBtn.create();
    alertDelete.show();

  }

  private void saveJournal(){
    JournalEntry newJournalEntry = new JournalEntry(detailsViewModelEntry.title, detailsViewModelEntry.date, detailsViewModelEntry.startTime, detailsViewModelEntry.endTime,detailsViewModelEntry.location);
    if(existFlag == 1){
      detailsViewModelEntry.insert(newJournalEntry);
    }
    else{
      newJournalEntry.setUid(UUID.fromString(dataEntry.uid));
      detailsViewModelEntry.update(newJournalEntry);
    }
    controllerNav.popBackStack();
  }

  private static final HashMap<String, Integer> mapMonth = new HashMap<>();
  static {
    String[] monthsArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    for (int i = 0; i < monthsArray.length; i++) {
      mapMonth.put(monthsArray[i], i + 1);
    }
  }

  private boolean dateValid(String d) {

    return true;
  }

  private boolean rangeTimeIsValid(){
    if(startFlag && endFlag){

      int hourStart,minuteStart,hourEnd,minuteEnd;
      minuteStart = Integer.parseInt(detailsViewModelEntry.startTime.substring(detailsViewModelEntry.startTime.indexOf(':') + 1));
      minuteEnd = Integer.parseInt(detailsViewModelEntry.endTime.substring(detailsViewModelEntry.endTime.indexOf(':') + 1));
      hourStart = Integer.parseInt(detailsViewModelEntry.startTime.substring(0, detailsViewModelEntry.startTime.indexOf(':')));
      hourEnd = Integer.parseInt(detailsViewModelEntry.endTime.substring(0, detailsViewModelEntry.endTime.indexOf(':')));

      if (hourStart < hourEnd) return true;
      else return hourStart == hourEnd && minuteStart <= minuteEnd;

    }
    return false;
  }


  final LifecycleEventObserver timeObserver = (source, event) -> {
    if (event.equals(Lifecycle.Event.ON_RESUME)) {
      if (entryBackStackNav.getSavedStateHandle().contains(getString(R.string.TYPE_START))) {
        stringStart = entryBackStackNav.getSavedStateHandle().get(getString(R.string.TYPE_START));
        detailsViewModelEntry.startTime = stringStart;
        startFlag = true;
      }
      if(entryBackStackNav.getSavedStateHandle().contains(getString(R.string.TYPE_END))){
        stringEnd = entryBackStackNav.getSavedStateHandle().get(getString(R.string.TYPE_END));
        detailsViewModelEntry.endTime = stringEnd;
        endFlag = true;
      }
      updateUI();
      if (!detailsViewModelEntry.startTime.equals("Start Time") && !detailsViewModelEntry.endTime.equals("End Time") && !rangeTimeIsValid()) {
        timeFlag = false;
        Toast.makeText(getContext(), "Start should be before End", Toast.LENGTH_SHORT).show();
      }
      else if(detailsViewModelEntry.startTime.equals("Start Time") || detailsViewModelEntry.endTime.equals("End Time")){
        timeFlag = false;
      }
      else {
        timeFlag = true;
      }
    }
  };

  final LifecycleEventObserver dateObserver = (source, event) -> {
    if (event.equals(Lifecycle.Event.ON_RESUME) && entryBackStackNav.getSavedStateHandle().contains(getString(R.string.DATE_KEY))) {
      String result = entryBackStackNav.getSavedStateHandle().get(getString(R.string.DATE_KEY));
      if (!dateValid(result)) {
        dateFlag = false;
        Toast.makeText(getContext(), "Wrong Date Entered", Toast.LENGTH_SHORT).show();
      }
      else{
        dateFlag = true;
        detailsViewModelEntry.date = result;
        updateUI();
      }
    }
  };

}