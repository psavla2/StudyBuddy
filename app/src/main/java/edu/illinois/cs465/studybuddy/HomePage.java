package edu.illinois.cs465.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);
        Log.d("home", "This is Home Page");
        ChipGroup filters = findViewById(R.id.home_page_chips);
        Tag[] tags = JsonReader.getTags(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        //get Shared preferences
        SharedPreferences myPrefs = getSharedPreferences("Onboarding", MODE_PRIVATE);
        String s = myPrefs.getString("forYou", "");
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Integer>>(){}.getType();
        Set<Integer> onboardingSet = gson.fromJson(s, setType);

        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            if (!onboardingSet.contains(t.id)) continue;
            chip.setEnsureMinTouchTargetSize(false);
            filters.addView(chip);
        }

        // filter
        ImageView filterButton = (ImageView) findViewById(R.id.home_page_filter_button);
        filterButton.setOnClickListener(v -> StartFilterSearch());

        //reset the onboarding set
        ImageView resetButton = (ImageView)findViewById(R.id.home_page_reset_button);
        resetButton.setOnClickListener(v -> resetOnboardingSet());
//
//        //search name
        ImageView searchButton = (ImageView)findViewById(R.id.home_page_search_button);
        searchButton.setOnClickListener(v -> StartSearchByName());

        ImageView forYou = (ImageView)findViewById(R.id.for_you_button);
        Integer[] onboarding_array = new Integer[onboardingSet.size()];
        onboarding_array = onboardingSet.toArray(onboarding_array);
        Integer[] finalOnboarding_array = onboarding_array;
        forYou.setOnClickListener(v -> StartCustomFilterSearch(finalOnboarding_array));

        Playlist[] playlists = JsonReader.getPlaylists(this); //grabs and converts the json of all playlist data into the playlist class.
        LinearLayout playlist_layout = findViewById(R.id.playlist_layout); // LinearLayout that holds all playlist cards in the home page.
        double max_playlist_height = 0; // max_playlist_height holds the height of the tallest container made using makePlaylist to later construct cards of consistent height.
        ArrayList<LinearLayout> playlist_containers = new ArrayList<LinearLayout>(); // playlist_containers will hold all linearlayouts built using makePlaylist.

        for (Playlist playlist : playlists) {
            Log.d("Next playlist ", playlist.Playlist.toString());
            LinearLayout playlist_view = PlaylistView.makePlaylist(this, playlist, tags);
            playlist_containers.add(playlist_view);
            if (playlist_view.getHeight() > max_playlist_height)
            {
                max_playlist_height = playlist_view.getHeight();
            }
            View button = playlist_view.getChildAt(0);
            button.setOnClickListener(v -> StartCustomFilterSearch(playlist.Tags));
        }
        for (LinearLayout container : playlist_containers){
            Log.d("Next playlist ", "container");
            //TODO - ADD CARD to back of linearlayout. (Add playlist_view to card)
            MaterialCardView card = PlaylistView.makePlaylistCard(this, container, (int) max_playlist_height);
            LinearLayout vert_temp = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            vert_temp.setLayoutParams(params);
            vert_temp.setGravity(Gravity.CENTER);

            card.addView(vert_temp);
            vert_temp.addView(container);
            LinearLayout for_margins = new LinearLayout(this);

            for_margins.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams for_margins_layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            for_margins_layout.topMargin = Math.round(5 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            for_margins_layout.bottomMargin = Math.round(5 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            for_margins.setLayoutParams(for_margins_layout);
            for_margins.setGravity(Gravity.CENTER);
            for_margins.addView(card);
            playlist_layout.addView(for_margins);
        }
    }

    //go to the home page
    private void startHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
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
        startActivity(intent);
    }

    private void StartSearchByName() {
        Intent intent = new Intent(this, NameSearchActivity.class);
        startActivity(intent);
    }

    private void StartCustomFilterSearch(java.io.Serializable value) {
        Intent intent = new Intent(this, FilterSearchActivity.class);
        intent.putExtra("filter_tags", value);
        startActivity(intent);
    }
}