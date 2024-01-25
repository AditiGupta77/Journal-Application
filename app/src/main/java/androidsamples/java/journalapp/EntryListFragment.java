package androidsamples.java.journalapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class EntryListFragment extends Fragment {

  JournalEntryListAdapter adapterEntryItem;
  NavBackStackEntry  entryBackStackNav;
  EntryListViewModel viewModelEntryList;
  MainViewModel viewModelMain;
  NavController controllerNav;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View viewInflated = inflater.inflate(R.layout.fragment_entry_list, container, false);

    FloatingActionButton buttonFloatingAction = viewInflated.findViewById(R.id.btn_new_entry);

    Toolbar toolbarList = viewInflated.findViewById(R.id.list_toolbar);

    viewModelEntryList = new ViewModelProvider(this).get(EntryListViewModel.class);

    viewModelEntryList.getAllEntries().observe(getViewLifecycleOwner(),(List<JournalEntry> entries) -> adapterEntryItem.insertData(entries));

    MenuProvider providerMenu = new MenuProvider() {
      @Override
      public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.list_menu, menu);
      }

      @Override
      public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.info){
          navToInfo();
        }
        return false;
      }
    };
    toolbarList.addMenuProvider(providerMenu);

    ArrayList<JournalEntry> listJournalEntry = new ArrayList<>();
    // Assign list to ItemAdapter
    adapterEntryItem = new JournalEntryListAdapter(listJournalEntry);
    // Set the LayoutManager that this RecyclerView will use.
    RecyclerView recyclerViewEntries = viewInflated.findViewById(R.id.recyclerView);
    recyclerViewEntries.setLayoutManager(new LinearLayoutManager(getActivity()));
    // adapter instance is set to the recyclerview to inflate the items.
    recyclerViewEntries.setAdapter(adapterEntryItem);
    adapterEntryItem.setListener(this::openForUpdate);

    buttonFloatingAction.setOnClickListener(this::navigationtoEntry);

    return viewInflated;
  }

  private void navigationtoEntry(View v){
    EntryListFragmentDirections.AddEntryAction actionAddEntry = EntryListFragmentDirections.addEntryAction();
    actionAddEntry.setOpCreateUpdateDelete(1);
    controllerNav.navigate(actionAddEntry);
  }
  private void navToInfo(){
    NavDirections actionToInfo = EntryListFragmentDirections.toInfoAction();
    controllerNav.navigate(actionToInfo);
  }

  void openForUpdate(int pos, JournalEntry j){
    EntryListFragmentDirections.AddEntryAction actionAddEntry = EntryListFragmentDirections.addEntryAction();
    actionAddEntry.setJEntry(true);
    viewModelMain.journalData = JournalEntryData.fromJournal(j);
    actionAddEntry.setOptionalIndex(pos);
    actionAddEntry.setOpCreateUpdateDelete(2);
    Log.d("RECYCLER ITEM MOD",viewModelMain.journalData.toString());
    controllerNav.navigate(actionAddEntry);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    controllerNav = Navigation.findNavController(view);
    entryBackStackNav = Navigation.findNavController(view).getBackStackEntry(R.id.nav_graph);
    viewModelMain = new ViewModelProvider(entryBackStackNav).get(MainViewModel.class);
  }

}
