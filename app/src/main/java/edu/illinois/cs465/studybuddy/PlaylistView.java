package edu.illinois.cs465.studybuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

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
        LinearLayout toReturn = new LinearLayout(ctx);
        toReturn.setOrientation(LinearLayout.HORIZONTAL);
        toReturn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        toReturn.setGravity(Gravity.CENTER);

        Button playlist_name = new Button(ctx);
        LinearLayout.LayoutParams button_layout = new LinearLayout.LayoutParams(R.dimen.playlist_square, R.dimen.playlist_square);
        button_layout.rightMargin = R.dimen.playlist_margin;
        button_layout.weight = 0;
        playlist_name.setLayoutParams(button_layout);
        playlist_name.setText(playlist.playlist_name);

        HorizontalScrollView chip_container = new HorizontalScrollView(ctx);
        LinearLayout.LayoutParams chip_container_params = new LinearLayout.LayoutParams(R.dimen.chip_container_width, LinearLayout.LayoutParams.MATCH_PARENT);
        chip_container_params.weight = 0;
        chip_container.setLayoutParams(chip_container_params);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        ChipGroup filters = new ChipGroup(ctx);
        LinearLayout.LayoutParams filters_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        //get playlist tags
        Set<Integer> playlistSet = new HashSet<Integer>(Arrays.asList(playlist.tag_ids));


        for (Tag t : tags) {
            Chip chip = (Chip)inflater.inflate(R.layout.space_filter_chip, filters, false);
            chip.setId(t.id);
            chip.setText(t.tag);
            if (!playlistSet.contains(t.id)) continue;
            filters.addView(chip);
        }

        chip_container.addView(filters);
        toReturn.addView(playlist_name);
        toReturn.addView(chip_container);
        return toReturn;
    }
}
