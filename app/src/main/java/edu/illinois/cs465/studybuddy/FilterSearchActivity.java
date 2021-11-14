package edu.illinois.cs465.studybuddy;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

public class FilterSearchActivity extends AppCompatActivity {

    private TextView signal;
    private HashMap<Integer, StudySpace> spaces; // location ID -> StudySpace
    private List<Integer> sortedSpaceList;
    private HashMap<Integer, Integer> matchingTags; // location ID -> number of matching tags
    private HashSet<Integer> selectedTags;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        signal = findViewById(R.id.signal);

        spaces = new HashMap<>();
        selectedTags = new HashSet<>();
        matchingTags = new HashMap<>();
        sortedSpaceList = new ArrayList<>();
        ChipGroup filters = findViewById(R.id.filters_chip_group);
        Tag[] tags = JsonReader.getTags(this);

        StudySpace[] studySpaceArray = JsonReader.getSpaces(this);

        for (StudySpace s : studySpaceArray) {
            spaces.put(s.id, s);
            matchingTags.put(s.id, 0);
            sortedSpaceList.add(s.id);
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> ChangeTag(buttonView.getId(), isChecked));
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

    private void ChangeTag(int tagId, boolean added) {
        boolean tagsChanged = added ? selectedTags.add(tagId) : selectedTags.remove(tagId);
        if (tagsChanged) {
            for (StudySpace s : spaces.values()) {
                if (s.tags.contains(tagId)) {
                    Integer newMatchingCount = matchingTags.get(s.id);
                    // getOrDefault() is not allowed in API 21
                    if (newMatchingCount == null) newMatchingCount = 0;
                    newMatchingCount += (added ? 1 : -1);
                    matchingTags.put(s.id, newMatchingCount);
                }
            }
        }

        Collections.sort(sortedSpaceList, (a, b) -> {
            Integer matchingA = matchingTags.get(a);
            Integer matchingB = matchingTags.get(b);
            matchingA = matchingA == null ? 0 : matchingA;
            matchingB = matchingB == null ? 0 : matchingB;
            return -1 * matchingA.compareTo(matchingB); // -1 indicates reverse order
        });

        Log.d("SelectedTag", Integer.toString(tagId));
        Log.d("MatchingTags", matchingTags.toString());

        signal.setText(sortedSpaceList.toString());
    }
}
