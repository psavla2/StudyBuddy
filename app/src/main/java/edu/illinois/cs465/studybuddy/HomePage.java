package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        ChipGroup filters = findViewById(R.id.onboarding_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            filters.addView(chip);
        }
    }
}