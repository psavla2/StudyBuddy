package edu.illinois.cs465.studybuddy;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

public class PlaylistView {
    static LinearLayout makePlaylist(Context ctx, Playlist playlist){
        LinearLayout toReturn = new LinearLayout(ctx);
        toReturn.setOrientation(LinearLayout.VERTICAL);

        Button playlist_name = new Button(ctx);
        playlist_name.setLayoutParams(new LinearLayout.LayoutParams());

        return toReturn;
    }
}
