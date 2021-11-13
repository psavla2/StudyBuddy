package edu.illinois.cs465.studybuddy;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class utils{
    static int next_ID = 8000;


    static LinearLayout makePlaylist(Context context, String category_name, int playlist_length) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView category_header = new TextView(context);
        category_header.setText(category_name);
        layout.addView(category_header);
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        for(int i = 0; i < playlist_length; i++) {
            Button btnPlaylist = new Button(context);
            btnPlaylist.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btnPlaylist.setText("Playlist "+ (i));
            btnPlaylist.setId(next_ID);
            next_ID++;
            horizontal.addView(btnPlaylist);
        }
        layout.addView(horizontal);
        return layout;
    }
}