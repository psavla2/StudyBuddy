package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        Log.d("home", "This is Home Page");
        ChipGroup filters = findViewById(R.id.home_page_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        //get Shared preferences
        SharedPreferences myPrefs = getSharedPreferences("Onboarding", MODE_PRIVATE);
        String s = myPrefs.getString("forYou", "");
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Integer>>(){}.getType();
        Set<Integer> onboardingSet = gson.fromJson(s, setType);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            if (!onboardingSet.contains(t.id)) continue;
            filters.addView(chip);
        }

        // filter
        ImageView filterButton = (ImageView) findViewById(R.id.home_page_filter_button);
        filterButton.setOnClickListener(v -> StartFilterSearch());

        //reset the onboarding set
        ImageView resetButton = (ImageView)findViewById(R.id.home_page_reset_button);
        resetButton.setOnClickListener(v -> resetOnboardingSet());
//
//        //search name
        ImageView searchButton = (ImageView)findViewById(R.id.home_page_search_button);
        searchButton.setOnClickListener(v -> StartSearchByName());

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