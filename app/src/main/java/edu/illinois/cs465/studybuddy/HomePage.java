package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        ChipGroup filters = findViewById(R.id.homepage_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.homepage_tags, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            filters.addView(chip);
        }
    }

}