package edu.illinois.cs465.studybuddy;

import java.util.Locale;


public class LocationItem {
    private int imageDrawable;
    private String name;
    private String description;

    public LocationItem(String title, String subtitle) {
        imageDrawable = R.drawable.elcap;
        name = title;
        description = subtitle;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
