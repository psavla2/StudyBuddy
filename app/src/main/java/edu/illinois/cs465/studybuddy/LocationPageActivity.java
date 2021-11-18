package edu.illinois.cs465.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class LocationPageActivity extends AppCompatActivity {

    private HashMap<Integer, StudySpace> mSpacesMap; // location ID -> StudySpace
    private List<LocationItem> mSortedSpacesList;
    private HashMap<Integer, Integer> mMatchingTags; // location ID -> number of matching tags
    private HashSet<Integer> mSelectedTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extrasBundle = i.getExtras();
        setContentView(R.layout.activity_location);
        Integer[] filterTags = (extrasBundle == null) ? null : (Integer[]) extrasBundle.get("tags");
        String name = (extrasBundle == null) ? null : (String) extrasBundle.get("name");
        TextView tv = (TextView) findViewById(R.id.textView19);
        tv.setText(name);
        String desc = (extrasBundle == null) ? null : (String) extrasBundle.get("description");
        TextView tv2 = (TextView) findViewById(R.id.textView20);
        tv2.setText(desc);
        String image_id = (extrasBundle == null) ? null : (String) extrasBundle.get("image_id");
        int drawableResourceId = getResources().getIdentifier(image_id, "drawable", getPackageName());
        if (drawableResourceId == 0) drawableResourceId = R.drawable.elcap;
        Drawable d = getResources().getDrawable(drawableResourceId);
        ImageView image = (ImageView)findViewById(R.id.imageView1);
        image.setImageDrawable(d);

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
            mSortedSpacesList.add(new LocationItem(s));
        }

        AddStartingTags(filterTags);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Tag t : tags) {
            if (mSelectedTags.contains(t.id)) {
                Chip chip = (Chip) inflater.inflate(R.layout.space_filter_chip_uncheck, filters, false);
                chip.setId(t.id);
                chip.setText(t.tag);
                chip.setChecked(mSelectedTags.contains(t.id));
                filters.addView(chip);
            }
        }
    }

    private void AddStartingTags(Integer [] filterTags) {
        if (filterTags == null) return;

        for (Integer filterTag : filterTags) {
            mSelectedTags.add(filterTag);
            for (StudySpace s : mSpacesMap.values()) {
                Integer alreadyMatching = mMatchingTags.get(s.id);
                if (alreadyMatching == null) alreadyMatching = 0;
                if (s.tags.contains(filterTag)) mMatchingTags.put(s.id, alreadyMatching + 1);
            }
        }
    }
}
