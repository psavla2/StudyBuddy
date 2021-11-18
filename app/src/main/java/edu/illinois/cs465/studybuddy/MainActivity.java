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

            /*
            setContentView(R.layout.activity_main);
            // filter
            filterButton = (Button)findViewById(R.id.filter_button);
            filterButton.setOnClickListener(v -> StartFilterSearch());

            //reset the onboarding set
            resetButton = (Button)findViewById(R.id.reset_button);
            resetButton.setOnClickListener(v -> resetOnboardingSet());

            //search name
            Button searchButton = (Button)findViewById(R.id.name_search_button);
            searchButton.setOnClickListener(v -> StartSearchByName());
//            Log.d("find the s", s);

            //home page
            Button homePageButton = (Button)findViewById(R.id.home_page_button);
            homePageButton.setOnClickListener(v -> startHomePage());
            */

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
        startActivity(intent);
    }

    private void StartSearchByName() {
        Intent intent = new Intent(this, NameSearchActivity.class);
        startActivity(intent);
    }
}