package edu.illinois.cs465.studybuddy;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterSearchActivity extends AppCompatActivity {

    private TextView signal;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        signal = findViewById(R.id.signal);

        ChipGroup filters = findViewById(R.id.filters_chip_group);
        Tag[] tags = JsonReader.getTags(this);

        for (Tag t : tags) {
            Chip chip = new Chip(new ContextThemeWrapper(this, R.style.Widget_Material3_Chip_Filter));
            chip.setCheckable(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            chip.setLayoutParams(layoutParams);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                CharSequence newText;
                if (isChecked) {
                    newText = String.format(Locale.US, "Checked tag with id: %d", buttonView.getId());
                } else {
                    newText = String.format(Locale.US, "Unchecked tag with id: %d", buttonView.getId());
                }
                signal.setText(newText);
            });
            filters.addView(chip);
        }

        // recycler view static info
        // TODO:: make the list only take in values of a passed in list

        StudySpace[] studySpaces = JsonReader.getSpaces(this);
        mRecyclerView = findViewById(R.id.recycler_view);

        List<LocationItem> locationItemList = new ArrayList<>();
        for (StudySpace s : studySpaces) { // currently adding all cards
            locationItemList.add(new LocationItem(s.name, s.description));
        }

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter and pass in our locationItemList

        mAdapter = new MyAdapter(locationItemList, this);
        mRecyclerView.setAdapter(mAdapter);
    }
}
