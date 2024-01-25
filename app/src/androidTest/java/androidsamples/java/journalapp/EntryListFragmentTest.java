package androidsamples.java.journalapp;

import static androidx.test.espresso.Espresso.onView;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelStore;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

/**
 * Instrumented tests for the {@link EntryDetailsFragment}.
 */
@RunWith(AndroidJUnit4.class)
public class EntryListFragmentTest {
    @Before
    public void accessibility(){AccessibilityChecks.enable();}
    @Before
    public void room(){
        Context context = ApplicationProvider.getApplicationContext();
        Controller.initialize(context);
    }

    @Test
    public void testNavigationToEntryListFragment() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setViewModelStore(new ViewModelStore());

        FragmentScenario<EntryListFragment> entryListFragmentFragmentScenario
                = FragmentScenario.launchInContainer(EntryListFragment.class, null, R.style.Theme_JournalApp, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                EntryListFragment entryListFragment = new EntryListFragment();

                entryListFragment.getViewLifecycleOwnerLiveData().observeForever(viewLifecycleOwner -> {
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.nav_graph);
                        Navigation.setViewNavController(entryListFragment.requireView(), navController);
                    }
                });
                return entryListFragment;
            }
        });

        // Verify that performing a click changes the NavController's state
        onView(ViewMatchers.withId(R.id.btn_new_entry)).perform(ViewActions.click());
        assertThat(Objects.requireNonNull(navController.getCurrentDestination()).getId(), is(R.id.entryDetailsFragment));
    }

    @Test
    public void testNavigationToInfoFragment() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        navController.setViewModelStore(new ViewModelStore());

        FragmentScenario<EntryListFragment> entryListFragmentFragmentScenario
                = FragmentScenario.launchInContainer(EntryListFragment.class, null, R.style.Theme_JournalApp, new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader,
                                        @NonNull String className) {
                EntryListFragment entryListFragment = new EntryListFragment();
                entryListFragment.getViewLifecycleOwnerLiveData().observeForever(viewLifecycleOwner -> {
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.nav_graph);
                        Navigation.setViewNavController(entryListFragment.requireView(), navController);
                    }
                });
                return entryListFragment;
            }
        });

        // Verify that performing a click changes the NavController's state
        onView(ViewMatchers.withId(R.id.info)).perform(ViewActions.click());
        assertThat(Objects.requireNonNull(navController.getCurrentDestination()).getId(), is(R.id.infoFragment));

    }

}