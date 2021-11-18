package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button filterButton;
    private Button resetButton;
    private Button nameSearchButton;
    private Button homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the on-boarding set data from the shared preferences
        SharedPreferences myPrefs = getSharedPreferences("Onboarding", MODE_PRIVATE);
        String s = myPrefs.getString("forYou", "");
        //check whether the on-boarding page are set, if unset, we can let user to go the onboarding page
        if (s.length() == 0) {
            //go to the on-boarding page
            startOnboarding();
        } else {
            //go to the main to see the several activities we have
            // redirect to the home_page
            startHomePage();
            //after we go the the home_page, the main_activity page finish its job, so, we don't need the filter, onboarding reset, sarch button in mainPage

//             setContentView(R.layout.activity_main);
//             filter
//             filterButton = (Button)findViewById(R.id.filter_button);
//             filterButton.setOnClickListener(v -> StartFilterSearch());

//             reset the onboarding set
//             resetButton = (Button)findViewById(R.id.reset_button);
//             resetButton.setOnClickListener(v -> resetOnboardingSet());

//             search name
//             nameSearchButton = (Button)findViewById(R.id.name_search_button);
//             nameSearchButton.setOnClickListener(v -> StartSearchByName());

//             home page
//            homePageButton = (Button)findViewById(R.id.home_page_button);
//            homePageButton.setOnClickListener(v -> startHomePage());
        }


    }

    //go to the home page
    private void startHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    //clear the onboarding set
    private void resetOnboardingSet() {
        SharedPreferences myPrefs = getSharedPreferences("Onboarding", MODE_PRIVATE);
        myPrefs.edit().remove("forYou").commit();
        startActivity(new Intent(this, Onboarding.class));
    }

    private void startOnboarding() {
        Intent intent = new Intent(this, Onboarding.class);
        startActivity(intent);
    }

    private void StartFilterSearch() {
        Intent intent = new Intent(this, FilterSearchActivity.class);
        intent.putExtra("filter_tags", new Integer[] { 2, 3, 4 });
        startActivity(intent);
    }

    private void StartSearchByName() {
        Intent intent = new Intent(this, NameSearchActivity.class);
        startActivity(intent);
    }
}
