package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Locale;

public class Onboarding extends AppCompatActivity {

    private HashSet<Integer> selectedTags;
    private Button filterSubmit;
    private Button btnDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        selectedTags = new HashSet<>();
        SharedPreferences myPrefs = getPreferences(MODE_PRIVATE);
        ChipGroup filters = findViewById(R.id.onboarding_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        filterSubmit = findViewById(R.id.button3);
        Gson gson = new Gson();

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedTags.add(buttonView.getId());
                } else {
                    selectedTags.remove(buttonView.getId());

                }
            });
            filters.addView(chip);
        }

        //button to submit
        filterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // transfer json to store in sharedPreference
                String json = gson.toJson(selectedTags);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("forYou", json);
                editor.apply();
                editor.commit();
                startMain();
            }
        });

        //Debug Button
        btnDebug = findViewById(R.id.buttonDebug);
        btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = myPrefs.getString("forYou", "");
                Log.d("findTheForYou", s);
            }
        });

    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}