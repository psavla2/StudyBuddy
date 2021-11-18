package edu.illinois.cs465.studybuddy;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JsonReader {

    private static String getJson(String jsonFileName, Context ctx) {
        String json;
        try {
            InputStream inputStream = ctx.getAssets().open(jsonFileName);
            int size = inputStream.available();
            byte[] buf = new byte[size];
            int read = inputStream.read(buf);
            inputStream.close();
            json = new String(buf, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static StudySpace[] getSpaces(Context ctx) {
        String jsonData = getJson("spaces.json", ctx);
        return new Gson().fromJson(jsonData, StudySpace[].class);
    }

    public static Tag[] getTags(Context ctx) {
        String jsonData = getJson("tags.json", ctx);
        return new Gson().fromJson(jsonData, Tag[].class);
    }

    public static Playlist[] getPlaylists(Context ctx) {
        String jsonData = getJson("playlists.json", ctx);
        return new Gson().fromJson(jsonData, Playlist[].class);
    }
}
