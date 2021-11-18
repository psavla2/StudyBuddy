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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences myPrefs = getSharedPreferences("Onboarding", MODE_PRIVATE);
        String s = myPrefs.getString("forYou", "");
        if (s.length() == 0) {
            startOnboarding();
        } else {
            setContentView(R.layout.activity_main);
            filterButton = (Button)findViewById(R.id.filter_button);
            filterButton.setOnClickListener(v -> StartFilterSearch());
            resetButton = (Button)findViewById(R.id.reset_button);
            resetButton.setOnClickListener(v -> resetOnboardingSet());
            nameSearchButton = (Button)findViewById(R.id.name_search_button);
            nameSearchButton.setOnClickListener(v -> StartSearchByName());
            Log.d("find the s", s);
        }
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