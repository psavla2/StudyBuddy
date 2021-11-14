package edu.illinois.cs465.studybuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    private HashMap<Integer, StudySpace> mSpacesMap; // location ID -> StudySpace
    private List<Integer> mSortedSpacesList;
    private HashMap<Integer, Integer> mMatchingTags; // location ID -> number of matching tags
    private HashSet<Integer> mSelectedTags;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        mSpacesMap = new HashMap<>();
        mSelectedTags = new HashSet<>();
        mMatchingTags = new HashMap<>();
        mSortedSpacesList = new ArrayList<>();
        ChipGroup filters = findViewById(R.id.filters_chip_group);
        Tag[] tags = JsonReader.getTags(this);

        StudySpace[] studySpaceArray = JsonReader.getSpaces(this);

        for (StudySpace s : studySpaceArray) {
            mSpacesMap.put(s.id, s);
            mMatchingTags.put(s.id, 0);
            mSortedSpacesList.add(s.id);
        }

        mRecyclerView = findViewById(R.id.recycler_view);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> ChangeTag(buttonView.getId(), isChecked));
            filters.addView(chip);
        }
    }

    private void UpdateRecycler(List<Integer> idsToDisplay) {
        List<LocationItem> locationItemList = new ArrayList<>();
        for (Integer spaceId : idsToDisplay) {
            StudySpace space = mSpacesMap.get(spaceId);
            if (space != null) {
                locationItemList.add(new LocationItem(space.name, space.description));
            }
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
        boolean tagsChanged = added ? mSelectedTags.add(tagId) : mSelectedTags.remove(tagId);
        if (tagsChanged) {
            for (StudySpace s : mSpacesMap.values()) {
                if (s.tags.contains(tagId)) {
                    Integer newMatchingCount = mMatchingTags.get(s.id);
                    // getOrDefault() is not allowed in API 21
                    if (newMatchingCount == null) newMatchingCount = 0;
                    newMatchingCount += (added ? 1 : -1);
                    mMatchingTags.put(s.id, newMatchingCount);
                }
            }
        }

        Collections.sort(mSortedSpacesList, (a, b) -> {
            Integer matchingA = mMatchingTags.get(a);
            Integer matchingB = mMatchingTags.get(b);
            matchingA = matchingA == null ? 0 : matchingA;
            matchingB = matchingB == null ? 0 : matchingB;
            return -1 * matchingA.compareTo(matchingB); // -1 indicates reverse order
        });

        ArrayList<Integer> toDisplay = new ArrayList<>(mSortedSpacesList.size());

        for (Integer spaceId : mSortedSpacesList) {
            Integer matchingTags = mMatchingTags.get(spaceId);
            if (matchingTags == null || matchingTags == 0) break;
            toDisplay.add(spaceId);
        }

        UpdateRecycler(toDisplay);
    }
}
