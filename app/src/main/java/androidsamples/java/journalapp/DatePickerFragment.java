package androidsamples.java.journalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

  Calendar mCalendar;
  NavController navController;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mCalendar = Calendar.getInstance();

    return new DatePickerDialog(requireContext(), (view, y, m, d) -> {
      Calendar selectedDate = Calendar.getInstance();
      selectedDate.set(y, m, d);
      if (selectedDate.after(Calendar.getInstance())) {
        Toast.makeText(requireContext(), "Please enter past events", Toast.LENGTH_SHORT).show();
      } else {
        mCalendar = selectedDate;
        String selectedDateString = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        Objects.requireNonNull(navController.getPreviousBackStackEntry())
                .getSavedStateHandle().set(getString(R.string.DATE_KEY), selectedDateString);
      }
    }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navController = NavHostFragment.findNavController(this);
  }

}
