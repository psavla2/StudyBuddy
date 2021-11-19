package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;

public class LocationPageActivity extends AppCompatActivity {

    private String name;
    private String maps_id;
    private HashSet<Integer> mSelectedTags;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Bundle extrasBundle = i.getExtras();
        setContentView(R.layout.activity_location);

        Integer[] filterTags = (extrasBundle == null) ? null : (Integer[]) extrasBundle.get("tags");
        name = (extrasBundle == null) ? null : (String) extrasBundle.get("name");
        TextView tv = findViewById(R.id.textView19);
        tv.setText(name);
        String desc = (extrasBundle == null) ? null : (String) extrasBundle.get("description");
        TextView tv2 = findViewById(R.id.textView20);
        tv2.setText(desc);
        String image_id = (extrasBundle == null) ? null : (String) extrasBundle.get("image_id");

        int drawableResourceId = R.drawable.elcap;
        if (image_id != null) {
            drawableResourceId = getResources().getIdentifier(image_id, "drawable", getPackageName());
        }

        Drawable d = getResources().getDrawable(drawableResourceId);
        ImageView image = findViewById(R.id.imageView1);
        image.setImageDrawable(d);

        mSelectedTags = new HashSet<>();

        ChipGroup filters = findViewById(R.id.filters_chip_group);
        Tag[] tags = JsonReader.getTags(this);
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

        maps_id = (extrasBundle == null) ? null : (String) extrasBundle.get("maps_id");

        ImageView mapsButton = findViewById(R.id.location_page_gmaps_button);
        mapsButton.setOnClickListener(v -> OpenGoogleMaps());
    }

    private void AddStartingTags(Integer [] filterTags) {
        if (filterTags == null) return;

        Collections.addAll(mSelectedTags, filterTags);
    }

    private void OpenGoogleMaps() {
        try {
            String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
            String queryUrl = String.format("https://www.google.com/maps/search/?api=1&query=%s&query_place_id=%s?z=16", encodedName, maps_id);
            Log.d("Google Maps Query", queryUrl);
            Uri gMapsUri = Uri.parse(queryUrl);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gMapsUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            }
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Unsupported Encoding", name);
        }
    }
}
