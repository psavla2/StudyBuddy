package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;
import java.util.Locale;

public class Onboarding extends AppCompatActivity {

    private HashSet<Integer> selectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        selectedTags = new HashSet<>();
        ChipGroup filters = findViewById(R.id.onboarding_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);

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
    }
}