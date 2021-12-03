package edu.illinois.cs465.studybuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlaylistView {
    static LinearLayout makePlaylist(Context ctx, Playlist playlist, Tag[] tags){
        // Horizontal linear layout that holds 2 items, playlist button and filter chips
        LinearLayout toReturn = new LinearLayout(ctx);
        toReturn.setOrientation(LinearLayout.HORIZONTAL);
        toReturn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        toReturn.setGravity(Gravity.CENTER);

        // Creation of the playlist button which sets the text to the playlist name.
        Button playlist_name = new Button(ctx);
            // This is roundabout code to make sure I can specify layout parameters in dp format.
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams button_layout = new LinearLayout.LayoutParams(Math.round(140 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), Math.round(140 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)));
            // Separates the playlist button from the filter chips
        button_layout.rightMargin = Math.round(30 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        button_layout.leftMargin = Math.round(30 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        button_layout.weight = 0;
        playlist_name.setLayoutParams(button_layout);
        playlist_name.setText(playlist.Playlist);

        // Creates the scrollable chip container
        LinearLayout chip_container = new LinearLayout(ctx);
        chip_container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams chip_container_params = new LinearLayout.LayoutParams(Math.round(200 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), LinearLayout.LayoutParams.WRAP_CONTENT);
        chip_container_params.weight = 0;
        chip_container_params.gravity = Gravity.CENTER;
        chip_container.setLayoutParams(chip_container_params);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        ChipGroup filters = new ChipGroup(ctx);
        LinearLayout.LayoutParams filters_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        filters_params.bottomMargin = -Math.round(10 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        filters_params.topMargin = -Math.round(10 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        filters_params.gravity = Gravity.CENTER;

//        get playlist tags
        Set<Integer> playlistSet = new HashSet<Integer>(Arrays.asList(playlist.Tags));
//
//
        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            chip.setCheckable(false);
            if (!playlistSet.contains(t.id)) continue;
            chip.setEnsureMinTouchTargetSize(false);
            filters.addView(chip);
        }
        filters.setLayoutParams(filters_params);

        chip_container.addView(filters);
        toReturn.addView(playlist_name);
        toReturn.addView(chip_container);
//        toReturn.setPadding(0,Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)), 0, Math.round(20 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)));
        return toReturn;
    }

    static MaterialCardView makePlaylistCard(Context ctx, LinearLayout contents, int desired_height) {
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        MaterialCardView toReturn = new MaterialCardView(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Math.round(200 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)));
        params.gravity = Gravity.CENTER_VERTICAL;
        toReturn.setLayoutParams(params);

        return toReturn;
    }
}
